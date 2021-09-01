package com.cg.cropdeal.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.CropDatabase
import com.cg.cropdeal.model.Crops
import com.cg.cropdeal.model.Invoice
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminReportVM(application: Application):AndroidViewModel(application) {
    private var invoiceList : MutableLiveData<List<Invoice>>? = null
    private var currentInvoiceList : MutableList<Invoice>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private val cropDB = CropDatabase.getInstance(application.applicationContext).cropDao()
    private var cropsList : MutableLiveData<List<Crops>>? = null
    private var currentCropList : MutableList<Crops>? = null


    init {
        invoiceList = MutableLiveData()
        currentInvoiceList = mutableListOf()
        firebaseDatabase = FirebaseDatabase.getInstance()
        populateCropList()
    }

    private fun populateInvoiceList(uid: String?) {
        firebaseDatabase?.reference?.child(Constants.INVOICE)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentInvoiceList?.clear()
                for(child in snapshot.children){
                    val invoice = child.getValue(Invoice::class.java)

                    if (invoice != null) {
                        currentInvoiceList?.add(invoice)
                    }
                    invoiceList?.value = currentInvoiceList
                }
                    invoiceList?.value = currentInvoiceList
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun populateCropList() {
        firebaseDatabase?.reference?.child(Constants.CROPS)?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentCropList?.clear()
                for(child in snapshot.children){
                    val crop = child.getValue(Crops::class.java)
                    currentCropList?.add(crop!!)
                    cropsList?.value = currentCropList
                }
                cropsList?.value = currentCropList
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    fun getInvoice() : MutableLiveData<List<Invoice>>? = invoiceList
    fun getCropList():MutableLiveData<List<Crops>>?{
        return cropsList
    }

    fun getFilteredList(cropForFilter:String):List<Crops>{
        if(cropForFilter=="All")    return currentCropList!!
        var filteredList : MutableList<Crops> = mutableListOf()
        for(crop in currentCropList!!){
            if(crop.cropName==cropForFilter){
                filteredList.add(crop)
            }
        }
        return filteredList
    }

}