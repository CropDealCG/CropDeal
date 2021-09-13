package com.cg.cropdeal.model.repo

import com.cg.cropdeal.model.Constants
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

    private fun updateUserProfile(uid:String, userHashMap:HashMap<String,Any>){



        val reference = firebaseDB?.getReference(Constants.USERS)?.child(uid)
        reference?.updateChildren(userHashMap)



    }
}