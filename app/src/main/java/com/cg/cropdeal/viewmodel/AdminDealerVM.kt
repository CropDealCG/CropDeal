package com.cg.cropdeal.viewmodel

import android.R
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AdminDealerVM(application: Application):AndroidViewModel(application) {

    private val userIdDAO= UserIdDatabase.getInstance(application.applicationContext!!).userIdDao()
    private var usersList : MutableLiveData<List<Users>>? = null
    private var dealersList : MutableList<Users>? = null
    private var usersIdList : MutableLiveData<List<String>>?=  null
    private var dealersIdList : MutableList<String>? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var firebaseAuth : FirebaseAuth? = null

    init {
        usersList = MutableLiveData()
        dealersList= mutableListOf()
        usersIdList = MutableLiveData()
        dealersIdList = mutableListOf()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
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
    fun registerUser(name:String,email:String,password:String,view:View){
        firebaseAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                if(task.result.additionalUserInfo?.isNewUser!!)
                {
                    Toast.makeText(view.context,"Dealer $name Created", Toast.LENGTH_LONG).show()
//                    Constants.showSnackbar("Farmer $name Created",view)
                    CoroutineScope(Dispatchers.Default).launch {
//                                Log.d("Observables","${task.result?.user?.uid!!},${task.result?.additionalUserInfo?.username},${task.result?.user?.email!!}")
                        val userId = UsersIDRepo(task.result?.user?.uid!!,task.result?.user?.email!!)
                        userIdDAO.insert(userId)
                        val user = Users(name,email,"dealer","false","","", Payment(),true,0.0,0,1)
                        firebaseDatabase!!.reference.child(Constants.USERS)
                            .child(task.result?.user?.uid!!).setValue(user)
                        firebaseAuth!!.signOut()
                    }
                }else{
                    firebaseAuth!!.signOut()
                }
            }else{
                Toast.makeText(view.context,"Registration Failure: ${task.exception?.message}", Toast.LENGTH_LONG).show()
//                Constants.showSnackbar("Registration Failure: ${task.exception?.message}",view)
            }
        }
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








