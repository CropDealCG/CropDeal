package com.cg.cropdeal.viewmodel.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.repo.CropRepo
import com.cg.cropdeal.model.Crops

class CropPublishVM(application: Application) : AndroidViewModel(application) {
    private var cropRepo : CropRepo? = null
    private var crops : MutableLiveData<Crops>? = null
    private var isCropAdded : MutableLiveData<Boolean>? = null
    init{
        cropRepo = CropRepo(application)
        crops = cropRepo!!.returnCrop()
        isCropAdded = cropRepo!!.isCropAdded()
    }
    fun addCrops(uuid:String){
        cropRepo?.addCrops(uuid)
    }
    fun returnCrop():MutableLiveData<Crops>?{
        return crops
    }
    fun returnUUID():String{
        return cropRepo?.getUUID()!!
    }
    fun isCropAdded() : MutableLiveData<Boolean>? = isCropAdded
}