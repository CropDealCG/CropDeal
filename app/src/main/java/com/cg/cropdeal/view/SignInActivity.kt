package com.cg.cropdeal.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.databinding.ActivitySignInBinding
import com.cg.cropdeal.viewmodel.SignInVM

class SignInActivity : AppCompatActivity() {
    private lateinit var signInVM : SignInVM
    private lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInVM = ViewModelProvider(this).get(SignInVM::class.java)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInBtn.setOnClickListener {
            signInWithEmailPassword()
        }
        binding.NewUserT.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
        signInVM.getUserLiveData()?.observe(this,{
            if(it!=null){
                startActivity(Intent(this,MainActivity::class.java))
            }
        })
    }

    private fun signInWithEmailPassword() {
        val email = binding.emailLoginE.text.toString()
        val password = binding.passwordLoginE.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            signInVM.login(email,password)
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            Toast.makeText(this,"Please Enter Data", Toast.LENGTH_SHORT).show()
        }
    }
}