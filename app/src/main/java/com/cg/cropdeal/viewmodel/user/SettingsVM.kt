package com.cg.cropdeal.viewmodel.user

import androidx.appcompat.app.AlertDialog
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.repo.FirebaseQueryLiveData
import com.cg.cropdeal.model.repo.SettingsRepo
import com.cg.cropdeal.model.repo.UtilRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class SettingsVM(application: Application) : AndroidViewModel(application) {
   private var settingsRepo : SettingsRepo? = null
    private var utilRepo : UtilRepo? = null
    private val user = FirebaseAuth.getInstance().currentUser
    private val dbRef = FirebaseDatabase.getInstance()
        .getReference(Constants.USERS).child(user?.uid!!)
    private val liveData = FirebaseQueryLiveData(dbRef)

    init{
        settingsRepo = SettingsRepo()
        utilRepo = UtilRepo()
    }



        fun getLogoutDialog(context: Context,layout:Int):AlertDialog{
            return utilRepo?.customDialog(context,layout)!!
        }

    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return liveData
    }
}