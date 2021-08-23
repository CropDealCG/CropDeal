package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cg.cropdeal.model.CropRepo
import com.cg.cropdeal.model.Crops

class CropPublishVM(application: Application) : AndroidViewModel(application) {
    private var cropRepo : CropRepo? = null
    init{
        cropRepo = CropRepo(application)
    }
    fun addCrops(crop : Crops){
        cropRepo?.addCrops(crop)
    }
}