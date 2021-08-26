package com.cg.cropdeal.model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private var firebaseAuth: FirebaseAuth? = null
private var firebaseDB : FirebaseDatabase?=null
private var paymentLiveData:MutableLiveData<Payment>? = null
private var isPaymentDetailsAdded : MutableLiveData<Boolean>? = null
class PaymentRepo(private var application: Application) {
    init {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDB = FirebaseDatabase.getInstance()
        paymentLiveData = MutableLiveData()
    }

    fun addPaymentDetails(payment: Payment,uuid:String){
        paymentLiveData?.observe(ProcessLifecycleOwner.get()){
        firebaseDB?.reference?.child(Constants.PAYMENT)
            ?.child(uuid)?.setValue(it)
            isPaymentDetailsAdded?.postValue(true)
        }
    }

    fun isPaymentDetailsAdded(): MutableLiveData<Boolean>?{
        return isPaymentDetailsAdded
    }
}