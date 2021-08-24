package com.cg.cropdeal.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.ActivitySignUpBinding
import com.cg.cropdeal.model.UtilActivity
import com.cg.cropdeal.viewmodel.SignUpVM
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

private const val USERS = "users"

class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpVM : SignUpVM
    private lateinit var binding : ActivitySignUpBinding
    private var utilActivity = UtilActivity()

    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var type: String
    lateinit var isAdmin: String
    lateinit var date: TextView
    lateinit var time: TextView
    lateinit var signUpButton: Button
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
        name = binding.nameE
        //email = binding.emailE
        type = "farmer"
        isAdmin = "false"
        date = binding.selectedDateTV
        time = binding.selectedTimeTV
        signUpButton = binding.signUpBtn

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

        //Linking button to Time Picker
        binding.timeBtn.setOnClickListener {
            val timePickerDialog = signUpVM.selectTime(this)
            timePickerDialog.addOnPositiveButtonClickListener { _ ->
                binding.selectedTimeTV.text = "${timePickerDialog.hour}:${timePickerDialog.minute}"
            }
            timePickerDialog.show(supportFragmentManager,"Time")
        }
    }

    private fun signUpWithEmailPassword() {
        val email = binding.emailE.text.toString()
        val password = binding.passwordE.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            signUpVM.register(email,password)

            //call Users class
            val users = Users(name.text.toString(),email,type,isAdmin,date.text.toString(),time.text.toString())
            reference.child(UUID.randomUUID().toString()).setValue(users)

            startActivity(Intent(this,MainActivity::class.java))
        }else{
            utilActivity.showSnackbar("Please Enter Data",binding.emailE)
        }
    }
}
//aayushi branch commit