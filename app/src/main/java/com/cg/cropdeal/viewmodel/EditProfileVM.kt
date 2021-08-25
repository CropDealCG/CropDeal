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
import com.google.firebase.auth.FirebaseAuth


class EditProfileVM(application: Application): AndroidViewModel(application) {
    private var settingsRepo : SettingsRepo? = null
    private val context = getApplication<Application>().applicationContext

    private val user = FirebaseAuth.getInstance().currentUser
    private val dbRef = FirebaseDatabase.getInstance()
        .getReference(Constants.USERS).child(user?.uid!!)
    private val liveData = FirebaseQueryLiveData(dbRef)

    init {
        settingsRepo = SettingsRepo(application)
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?,username: String){
        settingsRepo?.uploadImageToCloudStorage(activity,imageFileUri,context,username)
    }

    fun updateUserProfileDetails(username:String){
        settingsRepo?.updateUserProfileDetails(username)
    }



    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return liveData
    }

}