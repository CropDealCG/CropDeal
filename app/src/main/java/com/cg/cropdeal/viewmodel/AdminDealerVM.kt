package com.cg.cropdeal.viewmodel

import android.R
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.Crops
import com.cg.cropdeal.model.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminDealerVM(application: Application):AndroidViewModel(application) {

    private var usersList : MutableLiveData<List<Users>>? = null
    private var dealersList : MutableList<Users>? = null
    private var usersIdList : MutableLiveData<List<String>>?=  null
    private var dealersIdList : MutableList<String>? = null
    init {
        usersList = MutableLiveData()
        dealersList= mutableListOf()
        usersIdList = MutableLiveData()
        dealersIdList = mutableListOf()
        populateList()
    }

    private fun populateList() {
        val rootRef = FirebaseDatabase.getInstance().reference

        rootRef.child(Constants.USERS).orderByChild("type").equalTo("dealer")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dealersList?.clear()
                    dealersIdList?.clear()
                    for(child in snapshot.children){
                        val dealer = child.getValue(Users::class.java)!!
                        val dealerId = child.key.toString()
                        dealersIdList?.add(dealerId)
                        dealersList?.add(dealer)
                        usersIdList?.value = dealersIdList
                        usersList?.value = dealersList
                    }
                    usersIdList?.value = dealersIdList
                    usersList?.value = dealersList
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun getDealerData(): MutableLiveData<List<Users>>? = usersList
    fun getDealerIdData() : MutableLiveData<List<String>>? = usersIdList
}








