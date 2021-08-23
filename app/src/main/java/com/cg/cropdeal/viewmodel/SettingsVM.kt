package com.cg.cropdeal.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.SettingsRepo

import kotlin.reflect.KClass

class SettingsVM(application: Application) : AndroidViewModel(application) {
   private var settingsRepo : SettingsRepo? = null
    private val context = getApplication<Application>().applicationContext

    init{
        settingsRepo = SettingsRepo(application)
    }

    fun sendFeedback(){
        val email = Intent(Intent.ACTION_SEND)

        val mail={"cropdeals@gmail.com"}.toString()
        email.putExtra(Intent.EXTRA_EMAIL, mail)
        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        email.type = "message/rfc822"

        startActivity(context, Intent.createChooser(email, "Choose an Email client :"),null)
    }
}