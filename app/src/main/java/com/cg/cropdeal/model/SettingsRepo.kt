package com.cg.cropdeal.model


import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri

import android.util.Log
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
import kotlin.collections.HashMap

class SettingsRepo(private var application: Application?) {
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



    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?,context: Context,
    username: String,dob:String){
        val fStorage = FirebaseStorage.getInstance()
            .reference.child(
                "img"+FirebaseAuth.getInstance().currentUser?.email
            )

        fStorage.putFile(imageFileUri!!).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase image URI",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    imageUploadSuccess(uri.toString(),username,dob)
                    val profile_image_ref =  context
                        .getSharedPreferences(FirebaseAuth.getInstance().currentUser?.email,0)

                    val editor = profile_image_ref.edit()
                    editor.putString("profile_image",uri.toString())
                    editor.apply()
                }
        }
            .addOnFailureListener{exception ->

                Log.e(activity.javaClass.simpleName,
                    exception.message,exception)
            }
    }
    fun imageUploadSuccess(imageURL : String,username: String,dob:String){
        //hideProgressDialog()

        userProfileImageURL = imageURL

        updateUserProfileDetails(username,dob)
    }

    fun updateUserProfileDetails(username:String,dob:String){
        val userHashMap = HashMap<String,Any>()



        if(username.isNotEmpty()){
            userHashMap[Constants.USERNAME] = username

        }
        if(dob.isNotEmpty()){
            userHashMap[Constants.DATE] = dob
        }


        updateUserProfile(userHashMap)

    }

    fun updateUserProfile(userHashMap:HashMap<String,Any>){


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


class FirebaseQueryLiveData : LiveData<DataSnapshot?> {
    private val query: Query
    private val listener: MyValueEventListener = MyValueEventListener()

    constructor(query: Query) {
        this.query = query
    }

    constructor(ref: DatabaseReference) {
        query = ref
    }

    override fun onActive() {
        Log.d(LOG_TAG, "onActive")
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        Log.d(LOG_TAG, "onInactive")
        query.removeEventListener(listener)
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(
                LOG_TAG,
                "Can't listen to query $query", databaseError.toException()
            )
        }
    }

    companion object {
        private const val LOG_TAG = "FirebaseQueryLiveData"
    }
}


