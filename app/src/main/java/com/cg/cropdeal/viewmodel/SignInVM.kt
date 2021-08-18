package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.AuthRepo
import com.google.firebase.auth.FirebaseUser

class SignInVM(application: Application) : AndroidViewModel(application) {
    private var authRepo: AuthRepo? = null
    private var userLiveData: MutableLiveData<FirebaseUser>? = null

    init{
        authRepo = AuthRepo(application)
        userLiveData = authRepo!!.getUserLiveData()
    }
    fun login(email: String?, password: String?) {
        authRepo?.login(email, password)
    }
    fun getUserLiveData(): MutableLiveData<FirebaseUser>? {
        return userLiveData
    }
}