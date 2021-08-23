package com.cg.cropdeal.viewmodel

import androidx.appcompat.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.SettingsRepo
import com.cg.cropdeal.model.UtilRepo
import com.google.firebase.auth.FirebaseAuth

import kotlin.reflect.KClass

class SettingsVM(application: Application) : AndroidViewModel(application) {
   private var settingsRepo : SettingsRepo? = null
    private var utilRepo : UtilRepo? = null
    private val context = getApplication<Application>().applicationContext


    init{
        settingsRepo = SettingsRepo(application)
        utilRepo = UtilRepo(application)
    }

    fun sendFeedback(){
        val email = Intent(Intent.ACTION_SEND)

        val mail={"cropdeals@gmail.com"}.toString()
        email.putExtra(Intent.EXTRA_EMAIL, mail)
        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        email.type = "message/rfc822"

        startActivity(context, Intent.createChooser(email, "Choose an Email client :"),null)
    }

        fun getLogoutDialog(context: Context,layout:Int):AlertDialog{
            return utilRepo?.customDialog(context,layout)!!
        }
}