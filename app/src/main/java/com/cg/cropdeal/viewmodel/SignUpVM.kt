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
    private var isNewUser : MutableLiveData<Boolean>?= null
    private var utilRepo : UtilRepo? = null
    private var signInFailed : MutableLiveData<Boolean>? = null

    init{
        authRepo = AuthRepo(application)
        userLiveData = authRepo!!.getUserLiveData()
        isNewUser = authRepo!!.isNewUser()
        signInFailed = authRepo!!.isSignInFailed()
        utilRepo = UtilRepo(application)
    }

    fun register(email : String,password : String){
        authRepo?.register(email,password)
    }
    fun selectDate(context: Context) : MaterialDatePicker<Long>{
        return utilRepo?.selectDate(context)!!
    }

    fun getUserData() : MutableLiveData<FirebaseUser>?{
        return userLiveData
    }
    fun isNewUser() : MutableLiveData<Boolean>?{
        return isNewUser
    }
    fun isSignInFailed():MutableLiveData<Boolean>? = signInFailed

}