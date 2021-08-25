package com.cg.cropdeal.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class CropRepo(private var application: Application?) {

    private val cropDao = CropDatabase.getInstance(application?.applicationContext!!).cropDao()
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var crops : MutableLiveData<Crops>? = null
    init{
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        crops = MutableLiveData()
    }
    fun addCrops(crop : Crops, uuid:String){
        Log.d("Observables","$crop")
        crops?.observe(ProcessLifecycleOwner.get(),{
            CoroutineScope(Dispatchers.Default).launch {
                cropDao.insert(it)
            }
            firebaseDatabase?.reference?.child("crops")?.child(uuid)?.setValue(it)
        })

    }

    private fun randomUUID() : String = UUID.randomUUID().toString()

    private fun uuidIsAvailable(uuid : String) : String{
        var list : List<Crops>?=null
        CoroutineScope(Dispatchers.Main).launch {
            val response = CoroutineScope(Dispatchers.Default).async {
                cropDao.getCropByID(uuid)
            }
            list = response.await()
            if(list?.isNotEmpty()!!)   uuidIsAvailable(randomUUID())
        }
        return uuid
    }
    fun getUUID():String{
        return uuidIsAvailable(randomUUID())
    }
    fun returnCrop():MutableLiveData<Crops>?{
        return crops
    }

}