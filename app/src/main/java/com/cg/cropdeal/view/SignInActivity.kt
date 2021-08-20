package com.cg.cropdeal.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.ViewDataBindingKtx
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.ActivitySignInBinding
import com.cg.cropdeal.databinding.CustomForgotPasswordDialogBinding
import com.cg.cropdeal.model.UtilActivity
import com.cg.cropdeal.viewmodel.SignInVM
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {
    private lateinit var signInVM : SignInVM
    private lateinit var binding : ActivitySignInBinding
    private lateinit var auth : FirebaseAuth
    private var RC_SIGN_IN = 12
    private var utilActivity = UtilActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInVM = ViewModelProvider(this).get(SignInVM::class.java)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(this)
        binding.signInBtn.setOnClickListener {
            signInWithEmailPassword()
        }
        binding.NewUserT.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
        binding.googleLoginBtn.setOnClickListener {
            val signInIntent = signInVM.getGoogleSignInClient()?.signInIntent
            startActivityForResult(signInIntent,RC_SIGN_IN)
        }
        binding.facebookLoginBtn.setReadPermissions("email", "public_profile")
        binding.facebookLoginBtn.registerCallback(signInVM.getFacebookCallBackManager(),object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result?.accessToken!!)
            }

            override fun onCancel() {
            }
            override fun onError(error: FacebookException?) {
               utilActivity.showSnackbar("${error?.message}",binding.facebookLoginBtn)
                 }

        })
        binding.forgotPasswordT.setOnClickListener {_->
            val dialog = signInVM.getForgotPasswordDialog(this,R.layout.custom_forgot_password_dialog)
            val customBinding = CustomForgotPasswordDialogBinding.inflate(layoutInflater)
            dialog.setView(customBinding.root)
            customBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            customBinding.btnOkay.setOnClickListener {
                if(customBinding.txtInput.text.isEmpty()){
                    customBinding.txtInput.error = "Please Enter Email"
                    customBinding.txtInput.requestFocus()
                }
                else{
                    auth.sendPasswordResetEmail(customBinding.txtInput.text.toString())
                        .addOnCompleteListener{
                            if(it.isSuccessful){
                                Toast.makeText(this,"Password Reset Mail Sent Successfully", Toast.LENGTH_LONG).show()
                                dialog.dismiss()
                            }
                            else{
                                Toast.makeText(this,"${it.exception?.message}", Toast.LENGTH_LONG).show()
                                dialog.dismiss()
                            }
                        }
                }
            }
            dialog.show()
        }

        signInVM.getUserLiveData()?.observe(this,{
            if(it!=null){
                updateUI(null)
            }
        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    utilActivity.showSnackbar(
                        "Authentication failed. ${task.exception?.message}",
                            binding.facebookLoginBtn)
                    updateUI(null)
                }
            }
    }

    private fun signInWithEmailPassword() {
        val email = binding.emailLoginE.text.toString()
        val password = binding.passwordLoginE.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            signInVM.login(email,password)
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            utilActivity.showSnackbar("Please Enter Data",binding.passwordLoginE)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser : FirebaseUser?){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            }catch(e : Exception){
                utilActivity.showSnackbar("${e.message}",binding.googleLoginBtn)
            }
        }
        else{
            signInVM.getFacebookCallBackManager()?.onActivityResult(requestCode,resultCode, data)
        }
    }
}