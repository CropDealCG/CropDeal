package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.Crops
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MarketVM(application: Application) : AndroidViewModel(application) {
    private var cropsList : MutableLiveData<List<Crops>>? = null
    private var currentCropList : MutableList<Crops>? = null
    private var bankDetailsAvailable : MutableLiveData<Boolean>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var firebaseAuth : FirebaseAuth? = null
    private var dataChanged : MutableLiveData<Boolean>?  = null

    init {
        cropsList = MutableLiveData()
        currentCropList = mutableListOf()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        bankDetailsAvailable = MutableLiveData()
        dataChanged = MutableLiveData()
        dataChanged!!.value = false
        populateList()
        checkBankDetails()
    }
    private fun populateList() {
        firebaseDatabase?.reference?.child(Constants.CROPS)?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataChanged!!.postValue(true)
                    currentCropList?.clear()
                    for(child in snapshot.children){
                        val crop = child.getValue(Crops::class.java)
                        currentCropList?.add(crop!!)
                        cropsList?.value = currentCropList
                    }
                cropsList?.value = currentCropList
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
                        if(snapshot.child("bank").value.toString().isEmpty()) bankDetailsAvailable!!.postValue(false)
                        else    bankDetailsAvailable!!.postValue(true)
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
    fun getFilteredList(cropForFilter:String):List<Crops>{
        if(cropForFilter=="All")    return currentCropList!!
        val filteredList : MutableList<Crops> = mutableListOf()
        for(crop in currentCropList!!){
                if(crop.cropName==cropForFilter){
                    filteredList.add(crop)
                }
        }
        return filteredList
    }

    fun areBankDetailsAvailable() : MutableLiveData<Boolean>? = bankDetailsAvailable
    fun isDataChanged() : MutableLiveData<Boolean>? = dataChanged
}