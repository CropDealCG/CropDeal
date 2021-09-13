package com.cg.cropdeal.viewmodel.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cg.cropdeal.model.repo.AdminSettingsRepo
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.repo.FirebaseQueryLiveData
import com.cg.cropdeal.model.repo.UtilRepo
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class AdminEditProfileVM(application: Application):AndroidViewModel(application) {
    private var utilRepo: UtilRepo? = null
    private var settingsRepo: AdminSettingsRepo? = null
    init {
        utilRepo = UtilRepo()
        settingsRepo = AdminSettingsRepo()
    }

    fun getDataSnapshotLiveData(uid: String): LiveData<DataSnapshot?> {
        val dbRef = FirebaseDatabase.getInstance()
            .getReference(Constants.USERS).child(uid)
        return FirebaseQueryLiveData(dbRef)
    }

    fun updateUserProfileDetails(uid:String,username:String,dob:String,vehicle:String){
        settingsRepo?.updateUserProfileDetails(uid,username,dob,vehicle)
    }

    fun selectDate(): MaterialDatePicker<Long> {
        return utilRepo?.selectDate()!!
    }

}