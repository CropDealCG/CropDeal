package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.CropRepo
import com.cg.cropdeal.model.Crops

class CropPublishVM(application: Application) : AndroidViewModel(application) {
    private var cropRepo : CropRepo? = null
    private var crops : MutableLiveData<Crops>? = null
    init{
        cropRepo = CropRepo(application)
        crops = cropRepo!!.returnCrop()
    }
    fun addCrops(crop : Crops,uuid:String){
        cropRepo?.addCrops(crop,uuid)
    }
    fun returnCrop():MutableLiveData<Crops>?{
        return crops
    }
    fun returnUUID():String{
        return cropRepo?.getUUID()!!
    }
}