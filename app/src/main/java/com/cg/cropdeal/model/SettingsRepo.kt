package com.cg.cropdeal.model

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.BoringLayout
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener
import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue

import com.google.firebase.database.DatabaseReference

import androidx.lifecycle.LiveData







private var firebaseAuth: FirebaseAuth? = null
private var userLiveData: MutableLiveData<FirebaseUser>? = null
private var firebaseDB:FirebaseDatabase?= null

class SettingsRepo(private var application: Application?) {


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
    username: String){
        val fStorage = FirebaseStorage.getInstance()
            .reference.child(
                FirebaseAuth.getInstance().currentUser?.email
                        + "." + Constants.getFileExtension(activity,imageFileUri)
            )

        fStorage.putFile(imageFileUri!!).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase image URI",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    val profile_image_ref =  context
                        .getSharedPreferences(Constants.PROFILE_IMAGE_REF,0)

                    val editor = profile_image_ref.edit()
                    editor.putString("profile_image",uri.toString())
                    editor.apply()


                    imageUploadSuccess(uri.toString(),username)
                }
        }
            .addOnFailureListener{exception ->

                Log.e(activity.javaClass.simpleName,
                    exception.message,exception)
            }
    }
    fun imageUploadSuccess(imageURL : String,username: String){
        //hideProgressDialog()

        userProfileImageURL = imageURL

        updateUserProfileDetails(username)
    }

    fun updateUserProfileDetails(username:String){
        val userHashMap = HashMap<String,Any>()



        if(username.isNotEmpty()){
            userHashMap[Constants.USERNAME] = username

        }


        updateUserProfile(userHashMap)

    }

    fun updateUserProfile(userHashMap:HashMap<String,Any>){


        val user = firebaseAuth?.currentUser
        val reference = firebaseDB?.getReference(Constants.USERS)?.child(user?.uid!!)
        reference?.updateChildren(userHashMap)

        //reference.setValue(userHashMap)

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


