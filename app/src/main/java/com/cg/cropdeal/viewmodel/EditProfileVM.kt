package com.cg.cropdeal.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.cg.cropdeal.model.SettingsRepo

class EditProfileVM(application: Application): AndroidViewModel(application) {
    private var settingsRepo : SettingsRepo? = null
    private val context = getApplication<Application>().applicationContext

    init {
        settingsRepo = SettingsRepo(application)
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?,username: String){
        settingsRepo?.uploadImageToCloudStorage(activity,imageFileUri,context,username)
    }

    fun updateUserProfileDetails(username:String){
        settingsRepo?.updateUserProfileDetails(username)
    }

    fun setUsername(view:View):String{
        return settingsRepo?.setUsername(view)!!
    }

    fun setEmailID():String? {
        return settingsRepo?.setEmailID()!!
    }

}