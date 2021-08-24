package com.cg.cropdeal.view

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import com.cg.cropdeal.databinding.AboutUsActivityBinding
import com.cg.cropdeal.viewmodel.SettingsVM

class AboutUsActivity:AppCompatActivity() {
        private lateinit var binding : AboutUsActivityBinding
        private lateinit var viewModel: SettingsVM
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        viewModel = ViewModelProvider(this).get(SettingsVM::class.java)
        binding = AboutUsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rateUSBtn1.setOnClickListener {

        }
        binding.sendFeedbackBtn1.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)

            val mail={"cropdeals@gmail.com"}.toString()
            email.putExtra(Intent.EXTRA_EMAIL, mail)
            email.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            email.type = "message/rfc822"

            startActivity(
                Intent.createChooser(email, "Choose an Email client :"), null
            )
        }
    }







}
