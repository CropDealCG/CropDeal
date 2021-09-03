package com.cg.cropdeal.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class AdminSettingsRepo {


    private var firebaseDB: FirebaseDatabase?= null

    init {
        firebaseDB =  FirebaseDatabase.getInstance()
    }

    fun updateUserProfileDetails(uid:String,username: String, dob: String,vehicle:String) {
        val userHashMap = HashMap<String,Any>()

        if(username.isNotEmpty()){
            userHashMap[Constants.USERNAME] = username

        }
        if(dob.isNotEmpty()){
            userHashMap[Constants.DATE] = dob
        }

        userHashMap[Constants.NO_OF_CARS] = vehicle


        updateUserProfile(uid,userHashMap)
    }

    fun updateUserProfile(uid:String,userHashMap:HashMap<String,Any>){



        val reference = firebaseDB?.getReference(Constants.USERS)?.child(uid)
        reference?.updateChildren(userHashMap)



    }
}