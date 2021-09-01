package com.cg.cropdeal.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.Invoice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InvoiceVM(application: Application) : AndroidViewModel(application)  {
    private var invoiceList : MutableLiveData<List<Invoice>>? = null
    private var currentInvoiceList : MutableList<Invoice>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var firebaseAuth : FirebaseAuth? = null
    private var userType = ""
    init {
        invoiceList = MutableLiveData()
        currentInvoiceList = mutableListOf()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        userType = application.getSharedPreferences("LoginSharedPref", Context.MODE_PRIVATE)
            ?.getString("userType","")!!
        populateList(firebaseAuth?.currentUser?.uid)
    }

    private fun populateList(uid: String?) {
        firebaseDatabase?.reference?.child(Constants.INVOICE)?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentInvoiceList?.clear()
                for(child in snapshot.children){
                    val invoice = child.getValue(Invoice::class.java)
                    if(userType=="farmer"){
                        if(invoice?.farmerId == uid!!)  currentInvoiceList?.add(invoice)
                    }
                    else{
                        if(invoice?.buyerId == uid!!)  currentInvoiceList?.add(invoice)
                    }
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