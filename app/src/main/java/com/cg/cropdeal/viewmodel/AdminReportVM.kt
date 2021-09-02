package com.cg.cropdeal.viewmodel

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminReportVM(application: Application):AndroidViewModel(application) {
    private var invoiceList : MutableLiveData<List<Invoice>>? = null
    private var currentInvoiceList : MutableList<Invoice>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var utilRepo : UtilRepo? = null
    private var cropsList : MutableLiveData<List<Crops>>? = null
    private var currentCropList : MutableList<Crops>? = null


    init {
        invoiceList = MutableLiveData()
        currentInvoiceList = mutableListOf()
        cropsList = MutableLiveData()
        currentCropList = mutableListOf()
        firebaseDatabase = FirebaseDatabase.getInstance()
        populateInvoiceList()
        populateCropList()
        utilRepo = UtilRepo(application)
    }

    private fun populateInvoiceList() {
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
        firebaseDatabase?.reference?.child(Constants.CROPS)
            ?.addValueEventListener(object : ValueEventListener{
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

    fun getFilteredCropList(cropForFilter:String):List<Crops>{
        if(cropForFilter=="All")    return currentCropList!!
        //val filteredList : MutableList<Crops> = mutableListOf()
        val filteredList = currentCropList?.filter { it.cropName==cropForFilter }
        return filteredList!!
    }

    fun getInvoiceListByCrop(cropForFilter: String):List<Invoice>{
        if(cropForFilter=="All") return currentInvoiceList!!
        val filteredList = currentInvoiceList?.filter { it.cropName==cropForFilter }
        return filteredList!!


    }

    fun selectDate(context: Context): MaterialDatePicker<Long> {
        return utilRepo?.selectDate(context)!!

    }

    fun getInvoiceListByDate(dateForFilter:String):List<Invoice> {
        return currentInvoiceList?.filter { it.date==dateForFilter }!!
    }

}