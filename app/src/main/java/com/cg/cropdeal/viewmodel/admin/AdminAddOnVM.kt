package com.cg.cropdeal.viewmodel.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminAddOnVM(application: Application) : AndroidViewModel(application) {
    private var addOnList : MutableLiveData<List<String>>? = null
    private var currentAddOnList : MutableList<String>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    init {
        addOnList = MutableLiveData()
        currentAddOnList = mutableListOf()
        firebaseDatabase = FirebaseDatabase.getInstance()
        populateList()
    }

    private fun populateList() {
        firebaseDatabase!!.reference.child(Constants.ADDONPATHLIST).child(Constants.ADDONPATH).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentAddOnList?.clear()
                for(child in snapshot.children){
                    currentAddOnList?.add(child.key.toString())
                    addOnList?.value = currentAddOnList
                }
                addOnList?.value = currentAddOnList
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    fun addCrop(crop : String){
        firebaseDatabase!!.reference.child(Constants.ADDONPATHLIST).child(Constants.ADDONPATH)
            .child(crop).setValue(0)
    }
    fun getAddOnList() : MutableLiveData<List<String>>? = addOnList
}