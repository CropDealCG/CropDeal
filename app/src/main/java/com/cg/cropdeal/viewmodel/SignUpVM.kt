package com.cg.cropdeal.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseUser
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.AuthRepo
import com.cg.cropdeal.model.UtilRepo
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker


class SignUpVM(application: Application) : AndroidViewModel(application) {

    private var authRepo: AuthRepo? = null
    private var userLiveData: MutableLiveData<FirebaseUser>? = null
    private var utilRepo : UtilRepo? = null

    init{
        authRepo = AuthRepo(application)
        userLiveData = authRepo!!.getUserLiveData()
        utilRepo = UtilRepo(application)
    }

    fun register(email : String,password : String){
        authRepo?.register(email,password)
    }
    fun selectDate(context: Context) : MaterialDatePicker<Long>{
        return utilRepo?.selectDate(context)!!
    }
    fun selectTime(context: Context) : MaterialTimePicker{
        return utilRepo?.selectTime(context)!!
    }
    fun getUserData() : MutableLiveData<FirebaseUser>?{
        return userLiveData
    }

}