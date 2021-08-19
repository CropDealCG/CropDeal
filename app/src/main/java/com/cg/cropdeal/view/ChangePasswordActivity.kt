package com.cg.cropdeal.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.databinding.ActivityChangePasswordBinding
import com.cg.cropdeal.viewmodel.ChangePasswordVM

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var viewModel : ChangePasswordVM
    private var _binding : ActivityChangePasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_change_password)
        viewModel = ViewModelProvider(this).get(ChangePasswordVM::class.java)
        _binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}