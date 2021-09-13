package com.cg.cropdeal.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot

import androidx.lifecycle.LiveData

import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.repo.FirebaseQueryLiveData
import com.cg.cropdeal.model.repo.SettingsRepo
import com.cg.cropdeal.model.repo.UtilRepo
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth


class EditProfileVM(application: Application): AndroidViewModel(application) {
    private var settingsRepo : SettingsRepo? = null
    private var utilRepo: UtilRepo? = null

    private val user = FirebaseAuth.getInstance().currentUser
    private val dbRef = FirebaseDatabase.getInstance()
        .getReference(Constants.USERS).child(user?.uid!!)
    private val liveData = FirebaseQueryLiveData(dbRef)

    init {
        settingsRepo = SettingsRepo()
        utilRepo = UtilRepo()
    }

    fun uploadImageToCloudStorage( imageFileUri: Uri?,username: String,dob:String,cars:String){
        settingsRepo?.uploadImageToCloudStorage(imageFileUri,getApplication<Application>().applicationContext!!,username,dob,cars)
    }

    fun updateUserProfileDetails(username:String,dob:String,cars:String){
        settingsRepo?.updateUserProfileDetails(username,dob,cars)
    }



    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return liveData
    }

    fun selectDate(): MaterialDatePicker<Long> {
                return utilRepo?.selectDate()!!
    }

}