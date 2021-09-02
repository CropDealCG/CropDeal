package com.cg.cropdeal.viewmodel

import android.R
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.Crops
import com.cg.cropdeal.model.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AdminDealerVM(application: Application):AndroidViewModel(application) {

    private var usersList : MutableLiveData<List<Users>>? = null
    private var dealersList : MutableList<Users>? = null
    private var usersIdList : MutableLiveData<List<String>>?=  null
    private var dealersIdList : MutableList<String>? = null
    init {
        usersList = MutableLiveData()
        dealersList= mutableListOf()
        usersIdList = MutableLiveData()
        dealersIdList = mutableListOf()
        populateList()
    }

    private fun populateList() {
        val rootRef = FirebaseDatabase.getInstance().reference

        rootRef.child(Constants.USERS).orderByChild("type").equalTo("dealer")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dealersList?.clear()
                    dealersIdList?.clear()
                    for(child in snapshot.children){
                        val dealer = child.getValue(Users::class.java)!!
                        val dealerId = child.key.toString()
                        dealersIdList?.add(dealerId)
                        dealersList?.add(dealer)
                        usersIdList?.value = dealersIdList
                        usersList?.value = dealersList
                    }
                    usersIdList?.value = dealersIdList
                    usersList?.value = dealersList
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun getDealerData(): MutableLiveData<List<Users>>? = usersList
    fun getDealerIdData() : MutableLiveData<List<String>>? = usersIdList
    fun storeExcelInStorage(context: Context, fileName: String, workbook: Workbook,view: View):Boolean {
        var isSuccess: Boolean
        val file = File(context.getExternalFilesDir(null), fileName)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)
            Log.e("Excel", "Writing file $file")
            Constants.showSnackbar("File created at data/com.cg.cropdeal/files/Dealers.xls"
                ,view)
            isSuccess = true
        } catch (e: IOException) {
            Log.e("Excel", "Error writing Exception: ", e)
            isSuccess = false
        } catch (e: Exception) {
            Log.e("Excel", "Failed to save file due to Exception: ", e)
            isSuccess = false
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return isSuccess

    }
}








