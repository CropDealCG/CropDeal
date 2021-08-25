package com.cg.cropdeal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.databinding.ActivitySignUpBinding
import com.cg.cropdeal.model.Users
import com.cg.cropdeal.model.UtilActivity
import com.cg.cropdeal.viewmodel.SignUpVM
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

private const val USERS = "users"

class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpVM : SignUpVM
    private lateinit var binding : ActivitySignUpBinding
    private var utilActivity = UtilActivity()
    private var userType = "farmer"
//    private lateinit var users: Users

    private lateinit var rootNode : FirebaseDatabase
    private lateinit var reference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpVM = ViewModelProvider(this).get(SignUpVM::class.java)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode.getReference().child(USERS)

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
                startActivity(Intent(this,NavigationActivity::class.java))
                finish()
            }
        })

        //Linking button to Time Picker
        binding.timeBtn.setOnClickListener {
            val timePickerDialog = signUpVM.selectTime(this)
            timePickerDialog.addOnPositiveButtonClickListener { _ ->
                binding.selectedTimeTV.text = "${timePickerDialog.hour}:${timePickerDialog.minute}"
            }
            timePickerDialog.show(supportFragmentManager,"Time")
        }
        binding.userTypeRG.setOnCheckedChangeListener { _, i ->
            when(i){
                binding.farmerRB.id-> userType = "farmer"
                binding.delaerRB.id-> userType = "dealer"
            }
        }
        binding.userTypeRG.check(binding.farmerRB.id)
    }

    private fun signUpWithEmailPassword() {
        val email = binding.emailE.editText?.text.toString()
        val password = binding.passwordE.editText?.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            signUpVM.register(email,password)
            val users = Users(binding.nameE.editText?.text.toString(),email,userType,"false",binding.selectedDateTV.text.toString(),binding.selectedTimeTV.text.toString())
            signUpVM.getUserData()?.observe(this,{user->
                signUpVM.isNewUser()?.observe(this,{isNew->
                    Log.d("Observables","${user?.email}\t$isNew")
                    if(user!=null){
                        if(isNew)   {
                            reference.child(user.uid).setValue(users)   //Addition to Firebase

                            startActivity(Intent(this,NavigationActivity::class.java))
                            finish()
                        }
                        else    {
                            Toast.makeText(this,"You are already an user",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,NavigationActivity::class.java))
                            finish()
                        }
                    }
                })
            })

            //call Users class
        }else{
            utilActivity.showSnackbar("Please Enter Data",binding.emailE)
        }
    }
}