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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.FirebaseQueryLiveData
import com.cg.cropdeal.model.SettingsRepo
import com.cg.cropdeal.model.UtilRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

import kotlin.reflect.KClass

class SettingsVM(application: Application) : AndroidViewModel(application) {
   private var settingsRepo : SettingsRepo? = null
    private var utilRepo : UtilRepo? = null
    private val context = getApplication<Application>().applicationContext

    private val user = FirebaseAuth.getInstance().currentUser
    private val dbRef = FirebaseDatabase.getInstance()
        .getReference(Constants.USERS).child(user?.uid!!)
    private val liveData = FirebaseQueryLiveData(dbRef)

    init{
        settingsRepo = SettingsRepo(application)
        utilRepo = UtilRepo()
    }



        fun getLogoutDialog(context: Context,layout:Int):AlertDialog{
            return utilRepo?.customDialog(context,layout)!!
        }

    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return liveData
    }
}