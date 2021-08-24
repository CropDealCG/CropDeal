package com.cg.cropdeal.view


import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.databinding.ActivitySignUpBinding
import com.cg.cropdeal.model.UtilActivity
import com.cg.cropdeal.viewmodel.SignUpVM
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpVM : SignUpVM
    private lateinit var binding : ActivitySignUpBinding
    private var utilActivity = UtilActivity()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpVM = ViewModelProvider(this).get(SignUpVM::class.java)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                val date = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(Date(dateInMillis))
                val cal = Calendar.getInstance()
                val years = cal.get(Calendar.YEAR) - date.substring(date.length-4).toInt()
                val months = cal.get(Calendar.MONTH) - date.substring(3,5).toInt() +1
                val days = cal.get(Calendar.DAY_OF_MONTH) - date.substring(0,2).toInt()
                var age = 0
                if(months>0 || (months==0 && days>0))
                    age = years
                else
                    age = years-1
                binding.selectedDateTV.text = "age: " + age.toString() + "yr"
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
        val email = binding.emailE.editText?.text.toString()
        val password = binding.passwordE.editText?.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            signUpVM.register(email,password)
            startActivity(Intent(this,NavigationActivity::class.java))
        }else{
            utilActivity.showSnackbar("Please Enter Data",binding.emailE)
        }
    }
}