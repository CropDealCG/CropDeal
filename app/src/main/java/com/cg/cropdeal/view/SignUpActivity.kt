package com.cg.cropdeal.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.ActivitySignUpBinding
import com.cg.cropdeal.model.*
import com.cg.cropdeal.model.repo.UtilRepo
import com.cg.cropdeal.view.admin.AdminActivity
import com.cg.cropdeal.viewmodel.SignUpVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*



class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpVM : SignUpVM
    private lateinit var binding : ActivitySignUpBinding

    private lateinit var progressDialog : AlertDialog
    private var userType = Constants.FARMER

    private lateinit var rootNode : FirebaseDatabase
    private lateinit var reference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpVM = ViewModelProvider(this).get(SignUpVM::class.java)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode.reference.child(Constants.USERS)

        progressDialog = UtilRepo().loadingDialog(this)
        //Hooks to all xml elements in activity_sign_up.xml using view binding

        //save the data in the Firebase on button click
        binding.signUpBtn.setOnClickListener {
            signUpWithEmailPassword()
        }

        binding.existingUserT.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }

        //Linking button to Date Selector
        binding.selectedDateTV.setOnClickListener {
            val datePickerDialog = signUpVM.selectDate()
            datePickerDialog.addOnPositiveButtonClickListener { dateInMillis->
                val date = SimpleDateFormat("MMM dd, yyyy",Locale.getDefault()).format(Date(dateInMillis))
                binding.selectedDateTV.text = date
            }
            datePickerDialog.show(supportFragmentManager,"Date")
        }
        signUpVM.getUserData()?.observe(this,{
            if(it!=null){
                progressDialog.show()
                reference.child(it.uid).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.child(Constants.ACTIVE.lowercase()).value.toString()=="true"){
                            if(getSharedPreferences(Constants.LOGINPREF,Context.MODE_PRIVATE).getString(Constants.USERTYPE,"").isNullOrEmpty())
                            {
                                reference.child(it.uid).addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(snapshot.exists()){
                                            val userType = snapshot.child(Constants.TYPE).value.toString()
                                            when {
                                                userType.isEmpty() -> {
                                                    progressDialog.dismiss()
                                                }
                                                userType==Constants.ADMIN -> {
                                                    val sharedPref = getSharedPreferences(Constants.LOGINPREF,Context.MODE_PRIVATE)?:return
                                                    with(sharedPref.edit()){
                                                        putString(Constants.USERTYPE,userType)
                                                        apply()
                                                    }
                                                    progressDialog.dismiss()
                                                    startActivity(Intent(this@SignUpActivity,
                                                        AdminActivity::class.java))
                                                    finish()
                                                }
                                                else -> {
                                                    val sharedPref = getSharedPreferences(Constants.LOGINPREF,Context.MODE_PRIVATE)?:return
                                                    with(sharedPref.edit()){
                                                        putString(Constants.USERTYPE,userType)
                                                        apply()
                                                    }
                                                    progressDialog.dismiss()
                                                    updateUI()
                                                }
                                            }

                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }

                                })
                            }
                            else{
                                progressDialog.dismiss()
                                val sharedPref = getSharedPreferences(Constants.LOGINPREF,Context.MODE_PRIVATE)?:return
                                if(sharedPref.getString(Constants.USERTYPE,"")==Constants.ADMIN){
                                    startActivity(Intent(this@SignUpActivity, AdminActivity::class.java))
                                    finish()
                                }
                                else    updateUI()
                            }
                        }
                        else    {
                            progressDialog.dismiss()
                            Constants.showSnackbar(getString(R.string.accountDisabled),binding.root)
                            FirebaseAuth.getInstance().signOut()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })

            }
        })
        signUpVM.isSignInFailed()?.observe(this,{
            if(it!=null && it==true){
                progressDialog.dismiss()
            }
        })



        binding.userTypeRG.setOnCheckedChangeListener { _, i ->
            when(i){
                binding.farmerRB.id-> userType = Constants.FARMER
                binding.delaerRB.id-> userType = Constants.DEALER
            }
        }
        binding.userTypeRG.check(binding.farmerRB.id)
    }

    private fun signUpWithEmailPassword() {
        progressDialog.show()
        val email = binding.emailE.editText?.text.toString()
        val password = binding.passwordE.editText?.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            signUpVM.register(email,password)
            val users = Users(binding.nameE.editText?.text.toString(),email,userType,"false"
                ,binding.selectedDateTV.text.toString(),"",
                Payment(),true,0.0,0,""
            )
            signUpVM.getUserData()?.observe(this,{user->
                signUpVM.isNewUser()?.observe(this,{isNew->
                    //Log.d("Observables","${user?.email}\t$isNew")
                    if(user!=null){
                        if(isNew)   {
                            reference.child(user.uid).setValue(users)   //Addition to Firebase
                            updateUI()
                        }
                        else    {
                            Toast.makeText(this,"You are already an user",Toast.LENGTH_LONG).show()
                            updateUI()
                        }
                    }
                })
            })

            //call Users class
        }else{
            progressDialog.dismiss()
            Constants.showSnackbar("Please Enter Data",binding.root)
        }
    }

    private fun updateUI() {
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
    }
}