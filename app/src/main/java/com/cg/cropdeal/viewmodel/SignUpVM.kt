package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseUser
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.AuthRepo


class SignUpVM(application: Application) : AndroidViewModel(application) {

    private var authRepo: AuthRepo? = null
    private var userLiveData: MutableLiveData<FirebaseUser>? = null

    init{
        authRepo = AuthRepo(application)
        userLiveData = authRepo!!.getUserLiveData()
    }

    fun register(email : String,password : String){
        authRepo?.register(email,password)
    }

}