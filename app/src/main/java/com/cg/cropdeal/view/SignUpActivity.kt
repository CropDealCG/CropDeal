package com.cg.cropdeal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.databinding.ActivitySignUpBinding
import com.cg.cropdeal.model.*
import com.cg.cropdeal.viewmodel.SignUpVM
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*



class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpVM : SignUpVM
    private lateinit var binding : ActivitySignUpBinding
    private var utilActivity = UtilActivity()
    private lateinit var progressDialog : AlertDialog
    private var userType = Constants.FARMER
//    private lateinit var users: Users

    private lateinit var rootNode : FirebaseDatabase
    private lateinit var reference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpVM = ViewModelProvider(this).get(SignUpVM::class.java)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode.getReference().child(Constants.USERS)

        progressDialog = UtilRepo(application).loadingDialog(this)
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
        binding.DOBbtn.setOnClickListener {
            val datePickerDialog = signUpVM.selectDate(this)
            datePickerDialog.addOnPositiveButtonClickListener { dateInMillis->
                val date = SimpleDateFormat("MMM dd, yyyy",Locale.getDefault()).format(Date(dateInMillis))
                binding.selectedDateTV.text = date
            }
            datePickerDialog.show(supportFragmentManager,"Date")
        }

        signUpVM.getUserData()?.observe(this,{
            if(it!=null){
                updateUI()
            }
        })
        signUpVM.isSignInFailed()?.observe(this,{
            if(it!=null && it==true){
                progressDialog.dismiss()
            }
        })

        //Linking button to Time Picker
        val cal  = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE)
        binding.selectedTimeTV.setText("$hour:$min")

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
                ,binding.selectedDateTV.text.toString(),binding.selectedTimeTV.text.toString(),
                Payment()
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
            utilActivity.showSnackbar("Please Enter Data",binding.emailE)
        }
    }

    private fun updateUI() {
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
    }
}