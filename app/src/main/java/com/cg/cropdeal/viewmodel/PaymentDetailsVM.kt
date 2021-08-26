package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cg.cropdeal.model.SettingsRepo

private var settingsRepo:SettingsRepo? = null
class PaymentDetailsVM(application: Application):AndroidViewModel(application) {
    init{
        settingsRepo = SettingsRepo(application)
    }
    fun uploadPaymentDetails(bank:String,account: Long,ifsc:String) {
        settingsRepo?.uploadPaymentDetails(bank,account,ifsc)
    }
}