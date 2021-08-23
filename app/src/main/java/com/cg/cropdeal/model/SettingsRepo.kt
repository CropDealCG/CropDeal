package com.cg.cropdeal.model

import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private var firebaseAuth: FirebaseAuth? = null
private var userLiveData: MutableLiveData<FirebaseUser>? = null
class SettingsRepo(private var application:Application) {
    init {
        firebaseAuth = FirebaseAuth.getInstance()
        userLiveData = MutableLiveData()

        if (firebaseAuth!!.currentUser != null) {
            userLiveData!!.postValue(firebaseAuth!!.currentUser)
        }
    }

    fun getUserDetails(){
        val user = FirebaseAuth.getInstance().currentUser
        //val reference = FirebaseDatabase.getInstance().getReference(Constants.USERS).child(user?.uid!!)
    }


}