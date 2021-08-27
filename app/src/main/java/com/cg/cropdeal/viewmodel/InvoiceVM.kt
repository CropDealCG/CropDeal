package com.cg.cropdeal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Invoice
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InvoiceVM(application: Application) : AndroidViewModel(application)  {
    private var invoiceList : MutableLiveData<List<Invoice>>? = null
    private var currentInvoiceList : MutableList<Invoice>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    init {
        invoiceList = MutableLiveData()
        currentInvoiceList = mutableListOf()
        firebaseDatabase = FirebaseDatabase.getInstance()
        populateList()
    }

    private fun populateList() {
        firebaseDatabase?.reference?.child("invoice")?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentInvoiceList?.clear()
                for(child in snapshot.children){
                    val invoice = child.getValue(Invoice::class.java)
                    currentInvoiceList?.add(invoice!!)
                    invoiceList?.value = currentInvoiceList
                }
                invoiceList?.value = currentInvoiceList
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    fun getInvoice() : MutableLiveData<List<Invoice>>? = invoiceList
}