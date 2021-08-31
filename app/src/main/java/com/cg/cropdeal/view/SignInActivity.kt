package com.cg.cropdeal.view

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.ActivitySignInBinding
import com.cg.cropdeal.databinding.CustomForgotPasswordDialogBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.Payment
import com.cg.cropdeal.model.Users
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignInActivity : AppCompatActivity() {
    private lateinit var signInVM : SignInVM
    private lateinit var binding : ActivitySignInBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var userType : String
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var progressDialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInVM = ViewModelProvider(this).get(SignInVM::class.java)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog = UtilRepo(application).loadingDialog(this)
        auth = FirebaseAuth.getInstance()   //initialize the FirebaseAuth instance
        firebaseDatabase = FirebaseDatabase.getInstance()
        userType = ""
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(this)
//        AppEventsLogger.activateApp(application)
        binding.signInBtn.setOnClickListener {
            progressDialog.show()
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
                if(userType.isNotEmpty())   googleResultLauncher.launch(signInVM.getGoogleSignInClient()?.signInIntent!!)
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
                            Constants.showSnackbar("${error?.message}",binding.facebookLoginBtn)
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

//        signInVM.getUserLiveData()?.observe(this,{
//            if(it!=null){
//                progressDialog.dismiss()
//                updateUI(it)
//            }
//        })
        signInVM.isSignInFailed()?.observe(this,{
            if(it!=null && it==true){
                progressDialog.dismiss()
            }
        })
    }

    private val googleResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                progressDialog.show()
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: Exception) {
                progressDialog.dismiss()
                Constants.showSnackbar("$e - ${e.message}", binding.root)
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
                        val user = Users(facebookUser?.displayName!!,facebookUser.email!!
                            ,userType,"false","","", Payment()
                        )
                        firebaseDatabase.reference.child("users").child(task.result.user?.uid!!).setValue(user)
                        updateUI(facebookUser)
                    } else {
                        // If sign in fails, display a message to the user.
                        updateUI(facebookUser)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Constants.showSnackbar(
                        "Authentication failed. ${task.exception?.message}",
                            binding.root)
                    updateUI(null)
                }
            }
    }

    private fun signInWithEmailPassword() {
        val email = binding.emailLoginE.editText?.text.toString()
        val password = binding.passwordLoginE.editText?.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            signInVM.login(email,password)
            signInVM.isSignInFailed()?.observe(this,{
                progressDialog.dismiss()
                if(it!=null){
                    if(!it){
                        updateUI(null)
                    }
                }
            })
//            if(auth.currentUser!=null){
//                progressDialog.dismiss()
//                startActivity(Intent(this,NavigationActivity::class.java))
//                finish()
//            }
        }else{
            progressDialog.dismiss()
            Constants.showSnackbar("Please Enter Data",binding.root)
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
                        val user = Users(googleUser?.displayName!!,googleUser.email!!,userType,
                            "false","","",Payment())
                        val sharedPref = getSharedPreferences("LoginSharedPref", Context.MODE_PRIVATE)
                        with(sharedPref!!.edit()){
                            putString("userType",userType)
                            apply()
                        }
                        firebaseDatabase.reference.child("users").child(task.result.user?.uid!!).setValue(user)
                        updateUI(googleUser)
                    } else {
                        firebaseDatabase.reference.child("users").child(task.result.user?.uid!!)
                            .addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val dbUserType = snapshot.child("type").value.toString()
                                    val sharedPref = getSharedPreferences("LoginSharedPref", Context.MODE_PRIVATE)
                                    with(sharedPref!!.edit()){
                                        putString("userType",dbUserType)
                                        apply()
                                    }
                                    if(userType!=dbUserType){
                                        Toast.makeText(applicationContext,"You are already registered as an $dbUserType",Toast.LENGTH_LONG).show()
                                    }
                                    updateUI(googleUser)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }

                            })



                    }
                }// If sign in fails, display a message to the user.
            }
    }

    private fun updateUI(currentUser : FirebaseUser?){
        progressDialog.dismiss()
        startActivity(Intent(this,NavigationActivity::class.java))
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            signInVM.getFacebookCallBackManager()?.onActivityResult(requestCode,resultCode, data)
    }
}