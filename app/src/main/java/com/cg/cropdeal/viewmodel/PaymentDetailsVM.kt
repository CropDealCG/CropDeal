package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.FirebaseQueryLiveData
import com.cg.cropdeal.model.SettingsRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

private var settingsRepo:SettingsRepo? = null
class PaymentDetailsVM(application: Application):AndroidViewModel(application) {
    private val user = FirebaseAuth.getInstance().currentUser
    private val dbRef = FirebaseDatabase.getInstance()
        .getReference(Constants.USERS).child(user?.uid!!).child(Constants.PAYMENT)
    private val liveData = FirebaseQueryLiveData(dbRef)
    init{
        settingsRepo = SettingsRepo(application)
    }
    fun uploadPaymentDetails(bank:String,account: Long,ifsc:String) {
        settingsRepo?.uploadPaymentDetails(bank,account,ifsc)
    }
    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return liveData
    }
}