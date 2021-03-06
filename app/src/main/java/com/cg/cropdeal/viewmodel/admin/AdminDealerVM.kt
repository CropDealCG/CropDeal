package com.cg.cropdeal.viewmodel.admin

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.*
import com.cg.cropdeal.model.database.UserIdDatabase
import com.cg.cropdeal.model.UsersID
import com.google.android.material.snackbar.Snackbar
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
                        val userId = UsersID(task.result?.user?.uid!!,task.result?.user?.email!!)
                        userIdDAO.insert(userId)
                        val user = Users(name,email,"dealer","false","","", Payment(),true,0.0,0,"")
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.ms-excel")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents")
            }
            val uri: Uri? = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            try {
                if(uri!=null){
                    workbook.write(resolver.openOutputStream(uri))
                    Snackbar.make(view,"File created at /Documents/Dealers.xls", Snackbar.LENGTH_LONG)
                        .setAction("OPEN FILE") {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(uri,"application/vnd.ms-excel")
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            try{
                                getApplication<Application>().startActivity(intent)
                            }catch (e : ActivityNotFoundException){
                                Constants.showSnackbar("No application found which can open the file",view)
                            }
                        }.setAnimationMode(Snackbar.ANIMATION_MODE_FADE).setActionTextColor(Color.parseColor("#ffff2222"))
                        .show()
                }
//                Log.e("Excel", "Writing file $file")
                isSuccess = true
            }catch (e: IOException) {
                Log.e("Excel", "Error writing Exception: ", e)
                isSuccess = false
            } catch (e: Exception) {
                Log.e("Excel", "Failed to save file due to Exception: ", e)
                isSuccess = false
            } finally {
                try {

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

        }else{
            @Suppress("DEPRECATION")
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName)
            var fileOutputStream: FileOutputStream? = null
            try {

                fileOutputStream = FileOutputStream(file)
                workbook.write(fileOutputStream)
                Log.e("Excel", "Writing file $file")
                Constants.showSnackbar("File created at Documents/Dealers.xls"
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
                    fileOutputStream?.close()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

        return isSuccess

    }
}








