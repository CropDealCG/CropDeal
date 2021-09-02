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
    fun getDealerData(): MutableLiveData<List<Users>>?{

       usersList = MutableLiveData()
        dealersList= mutableListOf()
        val rootRef = FirebaseDatabase.getInstance().getReference()

        val usersdRef = rootRef.child(Constants.USERS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.getChildren()) {
                       val user= data.getValue(Users::class.java)
                        val type = user?.type
                        if (type == "dealer") {
                            dealersList?.add(user)
                            usersList?.value = dealersList
                        }
                        usersList?.value = dealersList
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
return usersList
    }
}








