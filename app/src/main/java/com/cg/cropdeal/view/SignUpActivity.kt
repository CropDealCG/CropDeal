package com.cg.cropdeal.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.databinding.ActivitySignUpBinding
import com.cg.cropdeal.model.UtilActivity
import com.cg.cropdeal.viewmodel.SignUpVM
import com.facebook.internal.Utility
import java.time.DayOfWeek
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpVM : SignUpVM
    private lateinit var binding : ActivitySignUpBinding
    private var utilActivity = UtilActivity()


    @RequiresApi(Build.VERSION_CODES.N)
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

        binding.DOBbtn.setOnClickListener {
            selectDate()
        }

        binding.timeBtn.setOnClickListener {
            selectTime()
        }
    }

    private fun signUpWithEmailPassword() {
        val email = binding.emailE.text.toString()
        val password = binding.passwordE.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            signUpVM.register(email,password)
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            utilActivity.showSnackbar("Please Enter Data",binding.emailE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun selectDate(){
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)


        val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                view, year, monthOfYear, dayOfMonth ->


            binding.selectedDateTV.setText("" + dayOfMonth + " " + month + ", " + year)

        }, year, month, day)

        datePicker.show()
    }

    fun selectTime(){
        val timePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        timePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                binding.selectedTimeTV.setText(String.format("%d : %d", hourOfDay, minute))
            }
        }, hour, minute, false)

        timePicker.show()
    }
}