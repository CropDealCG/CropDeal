package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Crops
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MarketVM(application: Application) : AndroidViewModel(application) {
    private var cropsList : MutableLiveData<List<Crops>>? = null
    private var currentCropList : MutableList<Crops>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var firebaseAuth : FirebaseAuth? = null

    init {
        cropsList = MutableLiveData()
        currentCropList = mutableListOf()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        initializeList()
    }

    private fun initializeList() {
        populateList()
    }

    private fun populateList() {
        firebaseDatabase?.reference?.child("crops")?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    currentCropList?.clear()
                    for(child in snapshot.children){
                        val crop = child.getValue(Crops::class.java)
                        currentCropList?.add(crop!!)
                        cropsList?.value = currentCropList
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    fun getCropList():MutableLiveData<List<Crops>>?{
        return cropsList
    }
}