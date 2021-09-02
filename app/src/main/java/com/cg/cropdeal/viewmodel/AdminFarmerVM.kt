package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminFarmerVM(application: Application) : AndroidViewModel(application) {
    private var currFarmerList : MutableList<Users>? = null
    private var currFarmerIdList : MutableList<String>? = null
    private var farmerList : MutableLiveData<List<Users>>? = null
    private var farmerIdList : MutableLiveData<List<String>>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    init {
        currFarmerIdList = mutableListOf()
        currFarmerList = mutableListOf()
        farmerList = MutableLiveData()
        farmerIdList = MutableLiveData()
        firebaseDatabase = FirebaseDatabase.getInstance()
        populateList()
    }

    private fun populateList() {
        firebaseDatabase!!.reference.child("users").orderByChild("type").equalTo("farmer")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    currFarmerList?.clear()
                    currFarmerIdList?.clear()
                    for(child in snapshot.children){
                        val farmer = child.getValue(Users::class.java)!!
                        val farmerId = child.key.toString()
                        currFarmerIdList?.add(farmerId)
                        currFarmerList?.add(farmer)
                        farmerList?.value = currFarmerList
                        farmerIdList?.value = currFarmerIdList
                    }
                    farmerList?.value = currFarmerList
                    farmerIdList?.value = currFarmerIdList
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
    fun getFarmerList() : MutableLiveData<List<Users>>? = farmerList
    fun getFarmerIdList() : MutableLiveData<List<String>>? = farmerIdList
}