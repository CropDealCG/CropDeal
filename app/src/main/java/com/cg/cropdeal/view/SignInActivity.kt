package com.cg.cropdeal.view

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.ActivitySignInBinding
import com.cg.cropdeal.databinding.CustomForgotPasswordDialogBinding
import com.cg.cropdeal.model.Users
import com.cg.cropdeal.model.UtilActivity
import com.cg.cropdeal.model.UtilRepo
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
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {
    private lateinit var signInVM : SignInVM
    private lateinit var binding : ActivitySignInBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var userType : String
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var progressDialog : AlertDialog
    private var utilActivity = UtilActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInVM = ViewModelProvider(this).get(SignInVM::class.java)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = UtilRepo(application).loadingDialog(this)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userType = ""
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(this)
//        AppEventsLogger.activateApp(application)
        binding.signInBtn.setOnClickListener {
            signInWithEmailPassword()
        }
        binding.NewUserT.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
        binding.googleLoginBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Are you a?")
            var dialogBuilder = dialog.create()
            dialog.setPositiveButton("Farmer",object:DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    userType = "farmer"
                }

            })
            dialog.setNegativeButton("Dealer",object:DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    userType = "dealer"
                }

            })
            dialog.setNeutralButton("Cancel",object:DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    dialogBuilder.dismiss()
                }

            })
            dialogBuilder = dialog.create()
            dialogBuilder.setCancelable(false)
            dialogBuilder.show()
            dialogBuilder.setOnDismissListener {
                if(userType.isNotEmpty())googleResultLauncher.launch(signInVM.getGoogleSignInClient()?.signInIntent!!)
            }
        }
        binding.facebookLoginBtn.setPermissions("email", "public_profile")
        binding.facebookLoginBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Are you a?")
            var dialogBuilder = dialog.create()
            dialog.setPositiveButton("Farmer",object:DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    userType = "farmer"
                }

            })
            dialog.setNegativeButton("Dealer",object:DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    userType = "dealer"
                }

            })
            dialog.setNeutralButton("Cancel",object:DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    dialogBuilder.dismiss()
                }

            })
            dialogBuilder = dialog.create()
            dialogBuilder.setCancelable(false)
            dialogBuilder.show()
            dialogBuilder.setOnDismissListener {
                if(userType.isNotEmpty()){
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
                }
            }
        }

        binding.forgotPasswordT.setOnClickListener {_->
            val dialog = signInVM.getForgotPasswordDialog(this,R.layout.custom_forgot_password_dialog)
            val customBinding = CustomForgotPasswordDialogBinding.inflate(layoutInflater)
            dialog.setView(customBinding.root)
            customBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            customBinding.btnOkay.setOnClickListener {
                if(customBinding.txtInput.editText?.text?.isEmpty()!!){
                    customBinding.txtInput.error = "Please Enter Email"
                    customBinding.txtInput.requestFocus()
                }
                else{
                    auth.sendPasswordResetEmail(customBinding.txtInput.editText?.text.toString())
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
                progressDialog.dismiss()
                updateUI(it)
            }
        })
        signInVM.isSignInFailed()?.observe(this,{
            Log.d("Observables","Dialog ert")
            if(it!=null && it==true){
                Log.d("Observables","Dialog")
                progressDialog.dismiss()
            }
        })
    }

    private val googleResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: Exception) {
                utilActivity.showSnackbar("$e - ${e.message}", binding.googleLoginBtn)
            }
        }
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val facebookUser = auth.currentUser
                    if (task.result.additionalUserInfo?.isNewUser!!) {
                        val user = Users(facebookUser?.displayName!!,facebookUser?.email!!,userType,"false","","")
                        firebaseDatabase.reference.child("users").child(task.result.user?.uid!!).setValue(user)
                        updateUI(facebookUser)
                    } else {
                        // If sign in fails, display a message to the user.
                        updateUI(facebookUser)
                    }
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
        progressDialog.show()
        val email = binding.emailLoginE.editText?.text.toString()
        val password = binding.passwordLoginE.editText?.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            signInVM.login(email,password)
            if(auth.currentUser!=null){
                startActivity(Intent(this,NavigationActivity::class.java))
                finish()
            }
        }else{
            progressDialog.cancel()
            progressDialog.hide()
            progressDialog.dismiss()
            utilActivity.showSnackbar("Please Enter Data",binding.passwordLoginE)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val googleUser = auth.currentUser
                    if (task.result.additionalUserInfo?.isNewUser!!) {
                        val user = Users(googleUser?.displayName!!,googleUser?.email!!,userType,"false","","")
                        firebaseDatabase.reference.child("users").child(task.result.user?.uid!!).setValue(user)
                        updateUI(googleUser)
                    } else {
                        // If sign in fails, display a message to the user.
                        updateUI(googleUser)
                    }
                }
            }
    }

    private fun updateUI(currentUser : FirebaseUser?){
        startActivity(Intent(this,NavigationActivity::class.java))
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            signInVM.getFacebookCallBackManager()?.onActivityResult(requestCode,resultCode, data)
    }
}