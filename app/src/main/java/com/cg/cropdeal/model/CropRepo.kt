package com.cg.cropdeal.model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CropRepo(private var application: Application?) {
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var crops : MutableLiveData<Crops>? = null
    init{
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        crops = MutableLiveData()
    }
    fun addCrops(crop : Crops){
        firebaseDatabase?.reference?.child("crops")?.child(UUID.randomUUID().toString())?.setValue(crop)
        crops?.postValue(crop)
    }



}