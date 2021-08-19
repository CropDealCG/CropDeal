package com.cg.cropdeal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.databinding.ActivitySignUpBinding
import com.cg.cropdeal.viewmodel.SignUpVM
import kotlin.math.sign

class SignUpActivity : AppCompatActivity() {
    lateinit var signUpVM : SignUpVM
    lateinit var binding : ActivitySignUpBinding

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
    }

    private fun signUpWithEmailPassword() {
        val email = binding.emailE.text.toString()
        val password = binding.passwordE.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            signUpVM.register(email,password)
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            Toast.makeText(this,"Please Enter Data",Toast.LENGTH_SHORT).show()
        }
    }
}