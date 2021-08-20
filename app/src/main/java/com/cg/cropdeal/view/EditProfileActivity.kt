package com.cg.cropdeal.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.ActivityEditProfileBinding
import com.cg.cropdeal.databinding.SettingsFragmentBinding
import com.cg.cropdeal.viewmodel.EditProfileVM
import com.cg.cropdeal.viewmodel.SettingsVM

class EditProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: EditProfileVM
    private var _binding : ActivityEditProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditProfileVM::class.java)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}