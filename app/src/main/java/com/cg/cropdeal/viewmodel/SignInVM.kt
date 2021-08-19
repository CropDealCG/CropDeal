package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.AuthRepo
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser

class SignInVM(application: Application) : AndroidViewModel(application) {
    private var authRepo: AuthRepo? = null
    private var userLiveData: MutableLiveData<FirebaseUser>? = null
    private var googleSignInClient : GoogleSignInClient? = null
    private var callbackManager : CallbackManager? = null

    init{
        authRepo = AuthRepo(application)
        userLiveData = authRepo!!.getUserLiveData()
        googleSignInClient = authRepo!!.getGoogleSignInClient()
        callbackManager = authRepo!!.getFacebookCallBackManager()
    }
    fun login(email: String?, password: String?) {
        authRepo?.login(email, password)
    }
    fun getUserLiveData(): MutableLiveData<FirebaseUser>? {
        return userLiveData
    }
    fun getGoogleSignInClient() : GoogleSignInClient?{
        return googleSignInClient
    }
    fun getFacebookCallBackManager() : CallbackManager?{
        return  callbackManager
    }
}