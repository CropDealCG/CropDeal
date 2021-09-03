package com.cg.cropdeal.model

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.MutableLiveData

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.GoogleSigninPromptBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthRepo(private var application: Application?) {

    private val userIdDAO= UserIdDatabase.getInstance(application?.applicationContext!!).userIdDao()
    private var firebaseAuth: FirebaseAuth? = null
    private var userLiveData: MutableLiveData<FirebaseUser>? = null
    private var loggedOutLiveData: MutableLiveData<Boolean>? = null
    private var newUser : MutableLiveData<Boolean>?= null
    private var signInFailed : MutableLiveData<Boolean>?=null
    private var selectedUserType : MutableLiveData<String>? = null
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(application?.getString(R.string.default_web_client_id)!!)
        .requestEmail().build()
    private var googleSignInClient : GoogleSignInClient? = null
    //facebook callbackmanager
    private var callbackManager : CallbackManager? = null

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        userLiveData = MutableLiveData()
        loggedOutLiveData = MutableLiveData()
        newUser = MutableLiveData()
        signInFailed = MutableLiveData()
        selectedUserType = MutableLiveData()
        if (firebaseAuth!!.currentUser != null) {
            userLiveData!!.postValue(firebaseAuth!!.currentUser)
            loggedOutLiveData!!.postValue(false)
        }
        googleSignInClient = GoogleSignIn.getClient(application?.applicationContext!!,gso)

        callbackManager = CallbackManager.Factory.create() //facebook login
    }

    fun register(email: String?, password: String?) {
        firebaseAuth!!.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        signInFailed!!.postValue(false)
                        userLiveData!!.postValue(firebaseAuth!!.currentUser)
                        newUser!!.postValue(task.result.additionalUserInfo?.isNewUser)
                        if(task.result.additionalUserInfo?.isNewUser!!)
                        {
                            CoroutineScope(Dispatchers.Default).launch {
//                                Log.d("Observables","${task.result?.user?.uid!!},${task.result?.additionalUserInfo?.username},${task.result?.user?.email!!}")
                                val userId = UsersIDRepo(task.result?.user?.uid!!,task.result?.user?.email!!)
                                userIdDAO.insert(userId)
                            }
                        }
                    } else {
                        signInFailed!!.postValue(true)
                        Toast.makeText(application!!.applicationContext,
                            "Registration Failure: ${task.exception?.message}" + task.exception?.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
    }

    fun login(email: String?, password: String?) {
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        FirebaseDatabase.getInstance().reference.child(Constants.USERS)
                            .child(task.result.user?.uid!!)
                            .addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    application!!.getSharedPreferences("LoginSharedPref", Context.MODE_PRIVATE)
                                        .edit {
                                            putString("userType",snapshot.child("type").value.toString())
                                            apply()
                                        }
                                        signInFailed!!.postValue(false)
                                        userLiveData!!.postValue(firebaseAuth!!.currentUser)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }

                            })

                    }
                    else {
                        signInFailed!!.postValue(true)
                        Toast.makeText(
                            application!!.applicationContext,
                            "Login Failure: " + task.exception?.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
    }
    fun userTypeDialog(context : Context) : AlertDialog{
        val dialog = MaterialAlertDialogBuilder(context)
        val customBinding = GoogleSigninPromptBinding.inflate(LayoutInflater.from(context))
        dialog.setView(customBinding.root)
//        dialog.setTitle("Choose One")
//        dialog.setMessage("Are you a?")
        var dialogBuilder = dialog.create()
        customBinding.typeInfo.text = "Please Select User Type"
        customBinding.typeRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                customBinding.dealerRadio.id ->{selectedUserType!!.postValue("dealer")}
                customBinding.farmerRadio.id -> {selectedUserType!!.postValue("farmer")}
            }
        }
        customBinding.typeRadioGroup.check(customBinding.farmerRadio.id)
        customBinding.okayBtn.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder = dialog.create()
        dialogBuilder.setCancelable(false)
        return dialogBuilder

    }
    fun logOut() {
        firebaseAuth!!.signOut()
        loggedOutLiveData!!.postValue(true)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser>? {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean>? {
        return loggedOutLiveData
    }
    fun getGoogleSignInClient() : GoogleSignInClient?{
        return googleSignInClient
    }
    fun getFacebookCallBackManager() : CallbackManager?{
        return callbackManager
    }
    fun isNewUser() : MutableLiveData<Boolean>?{
        return newUser
    }
    fun isSignInFailed() : MutableLiveData<Boolean>?{
        return signInFailed
    }
    fun selectedUserType() : MutableLiveData<String>? = selectedUserType

}