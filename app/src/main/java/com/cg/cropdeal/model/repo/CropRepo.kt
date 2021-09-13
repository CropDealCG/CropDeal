package com.cg.cropdeal.model.repo

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.database.CropDatabase
import com.cg.cropdeal.model.Crops
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class CropRepo(application: Application?) {

    private val cropDao = CropDatabase.getInstance(application?.applicationContext!!).cropDao()
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var crops : MutableLiveData<Crops>? = null
    private var isCropAdded : MutableLiveData<Boolean>? = null
    init{
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        crops = MutableLiveData()
        isCropAdded = MutableLiveData(false)
    }
    fun addCrops( uuid:String){
        crops?.observe(ProcessLifecycleOwner.get(),{
            CoroutineScope(Dispatchers.Default).launch {
                cropDao.insert(it) 
            //store in local room
            }
            firebaseDatabase?.reference?.child(Constants.CROPS)?.child(uuid)?.setValue(it)
            isCropAdded?.postValue(true)
        })

    }

    private fun randomUUID() : String = UUID.randomUUID().toString()

    //Checking if UUID against a crop is present or not
    private fun uuidIsAvailable(uuid : String) : String{
        var list : List<Crops>
        CoroutineScope(Dispatchers.Main).launch {
            val response = CoroutineScope(Dispatchers.Default).async {
                cropDao.getCropByID(uuid)
            }
            list = response.await()
            //if the list is not empty means UUID exists, recall function
            if(list.isNotEmpty())   uuidIsAvailable(randomUUID())
        }
        //else send the UUID
        return uuid
    }
    fun getUUID():String{
        return uuidIsAvailable(randomUUID())
    }
    fun returnCrop():MutableLiveData<Crops>?{
        return crops
    }
    fun isCropAdded() : MutableLiveData<Boolean>? = isCropAdded

}