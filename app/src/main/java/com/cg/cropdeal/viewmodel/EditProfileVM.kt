package com.cg.cropdeal.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.cg.cropdeal.model.FirebaseQueryLiveData
import com.cg.cropdeal.model.SettingsRepo
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot

import androidx.lifecycle.LiveData

import androidx.annotation.NonNull
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.UtilRepo
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth


class EditProfileVM(application: Application): AndroidViewModel(application) {
    private var settingsRepo : SettingsRepo? = null
    private var utilRepo:UtilRepo? = null
    private val context = getApplication<Application>().applicationContext

    private val user = FirebaseAuth.getInstance().currentUser
    private val dbRef = FirebaseDatabase.getInstance()
        .getReference(Constants.USERS).child(user?.uid!!)
    private val liveData = FirebaseQueryLiveData(dbRef)

    init {
        settingsRepo = SettingsRepo(application)
        utilRepo = UtilRepo()
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?,username: String,dob:String){
        settingsRepo?.uploadImageToCloudStorage(activity,imageFileUri,context,username,dob)
    }

    fun updateUserProfileDetails(username:String,dob:String){
        settingsRepo?.updateUserProfileDetails(username,dob)
    }



    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return liveData
    }

    fun selectDate(context: Context): MaterialDatePicker<Long> {
                return utilRepo?.selectDate(context)!!
    }

}