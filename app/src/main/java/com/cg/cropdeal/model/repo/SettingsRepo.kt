package com.cg.cropdeal.model.repo


import android.content.Context
import android.net.Uri

import androidx.lifecycle.MutableLiveData

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.DatabaseReference

import androidx.lifecycle.LiveData
import com.cg.cropdeal.model.Constants
import kotlin.collections.HashMap

class SettingsRepo {
    private var firebaseAuth: FirebaseAuth? = null
    private var userLiveData: MutableLiveData<FirebaseUser>? = null
    private var firebaseDB:FirebaseDatabase?= null
    private var userProfileImageURL: String = ""
    init {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDB =  FirebaseDatabase.getInstance()
        userLiveData = MutableLiveData()

        if (firebaseAuth!!.currentUser != null) {
            userLiveData!!.postValue(firebaseAuth!!.currentUser)
        }
    }



    fun uploadImageToCloudStorage(imageFileUri: Uri?,context: Context,
    username: String,dob:String,cars:String){
        val fStorage = FirebaseStorage.getInstance()
            .reference.child(
                "img"+FirebaseAuth.getInstance().currentUser?.email
            )

        fStorage.putFile(imageFileUri!!).addOnSuccessListener { taskSnapshot ->


            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    imageUploadSuccess(uri.toString(),username,dob,cars)
                    val profileImageRef =  context
                        .getSharedPreferences(FirebaseAuth.getInstance().currentUser?.email,0)

                    val editor = profileImageRef.edit()
                    editor.putString("profile_image",uri.toString())
                    editor.apply()
                }
        }
            .addOnFailureListener{


            }
    }
    private fun imageUploadSuccess(imageURL : String, username: String, dob:String, cars:String){
        //hideProgressDialog()

        userProfileImageURL = imageURL

        updateUserProfileDetails(username,dob,cars)
    }

    fun updateUserProfileDetails(username:String, dob:String, cars:String){
        val userHashMap = HashMap<String,Any>()



        if(username.isNotEmpty()){
            userHashMap[Constants.USERNAME] = username

        }
        if(dob.isNotEmpty()){
            userHashMap[Constants.DATE] = dob
        }

        userHashMap[Constants.NO_OF_CARS] = cars


        updateUserProfile(userHashMap)

    }

    private fun updateUserProfile(userHashMap:HashMap<String,Any>){


        val user = firebaseAuth?.currentUser
        val reference = firebaseDB?.getReference(Constants.USERS)?.child(user?.uid!!)
        reference?.updateChildren(userHashMap)

        //reference.setValue(userHashMap)

    }

    fun uploadPaymentDetails(bank:String,account: Long,ifsc:String) {
        val user = firebaseAuth?.currentUser

        val paymentHashMap = HashMap<String,Any>()

        paymentHashMap[Constants.BANK] = bank
        paymentHashMap[Constants.ACCOUNT] = account
        paymentHashMap[Constants.IFSC] = ifsc
        paymentHashMap[Constants.USERID] = user?.uid!!


        val reference = firebaseDB?.getReference(Constants.USERS)?.child(user.uid)
            ?.child(Constants.PAYMENT)
        reference?.updateChildren(paymentHashMap)
    }


}


class FirebaseQueryLiveData(ref: DatabaseReference) : LiveData<DataSnapshot?>() {
    private val query: Query = ref
    private val listener: MyValueEventListener = MyValueEventListener()

    /*constructor(query: Query) {
        this.query = query
    }*/

    override fun onActive() {
        query.addValueEventListener(listener)
    }

    override fun onInactive() {

        query.removeEventListener(listener)
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }


}


