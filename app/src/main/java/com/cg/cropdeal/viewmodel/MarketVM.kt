package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.CropDatabase
import com.cg.cropdeal.model.Crops
import com.cg.cropdeal.model.MarketAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MarketVM(application: Application) : AndroidViewModel(application) {
    private val cropDB = CropDatabase.getInstance(application.applicationContext).cropDao()
    private var cropsList : MutableLiveData<List<Crops>>? = null
    private var currentCropList : MutableList<Crops>? = null
    private var bankDetailsAvailable : MutableLiveData<Boolean>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var firebaseAuth : FirebaseAuth? = null

    init {
        cropsList = MutableLiveData()
        currentCropList = mutableListOf()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        bankDetailsAvailable = MutableLiveData()
        populateList()
        checkBankDetails()
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
    private fun checkBankDetails(){
        firebaseDatabase?.reference?.child(Constants.USERS)?.child(firebaseAuth?.currentUser?.uid!!)
            ?.child(Constants.PAYMENT)?.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        bankDetailsAvailable!!.postValue(true)
                    }
                    else{
                        bankDetailsAvailable!!.postValue(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
    fun getCropList():MutableLiveData<List<Crops>>?{
        return cropsList
    }
    fun areBankDetailsAvailable() : MutableLiveData<Boolean>? = bankDetailsAvailable
}