package com.cg.cropdeal.viewmodel

import android.app.Application
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.*
import com.cg.cropdeal.view.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminFarmerVM(application: Application) : AndroidViewModel(application) {

    private val userIdDAO= UserIdDatabase.getInstance(application.applicationContext!!).userIdDao()
    private var currFarmerList : MutableList<Users>? = null
    private var currFarmerIdList : MutableList<String>? = null
    private var farmerList : MutableLiveData<List<Users>>? = null
    private var farmerIdList : MutableLiveData<List<String>>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var firebaseAuth : FirebaseAuth? = null

    init {
        currFarmerIdList = mutableListOf()
        currFarmerList = mutableListOf()
        farmerList = MutableLiveData()
        farmerIdList = MutableLiveData()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
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
    fun registerUser(name:String,email:String,password:String,view:View){
        firebaseAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                if(task.result.additionalUserInfo?.isNewUser!!)
                {
                    Toast.makeText(view.context,"Farmer $name Created",Toast.LENGTH_LONG).show()
//                    Constants.showSnackbar("Farmer $name Created",view)
                    CoroutineScope(Dispatchers.Default).launch {
//                                Log.d("Observables","${task.result?.user?.uid!!},${task.result?.additionalUserInfo?.username},${task.result?.user?.email!!}")
                        val userId = UsersIDRepo(task.result?.user?.uid!!,task.result?.user?.email!!)
                        userIdDAO.insert(userId)
                        val user = Users(name,email,"farmer","false","","", Payment(),true,0.0,0,1)
                        firebaseDatabase!!.reference.child(Constants.USERS)
                            .child(task.result?.user?.uid!!).setValue(user)
                        firebaseAuth!!.signOut()
                    }
                }else{
                    firebaseAuth!!.signOut()
                }
            }else{
//                Constants.showSnackbar("Registration Failure: ${task.exception?.message}",view)
            }
        }
    }
    fun getFarmerList() : MutableLiveData<List<Users>>? = farmerList
    fun getFarmerIdList() : MutableLiveData<List<String>>? = farmerIdList
}