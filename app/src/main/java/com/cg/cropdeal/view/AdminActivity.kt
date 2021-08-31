package com.cg.cropdeal.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cg.cropdeal.databinding.AdminMainActivityBinding
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : AppCompatActivity() {
    private lateinit var binding : AdminMainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseAuth.getInstance()
    }
}