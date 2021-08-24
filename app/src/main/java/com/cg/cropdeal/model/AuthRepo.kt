package com.cg.cropdeal.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import com.cg.cropdeal.R
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthRepo(private var application: Application?) {

    private val userIdDAO= UserIdDatabase.getInstance(application?.applicationContext!!).userIdDao()
    private var firebaseAuth: FirebaseAuth? = null
    private var userLiveData: MutableLiveData<FirebaseUser>? = null
    private var loggedOutLiveData: MutableLiveData<Boolean>? = null
    private var newUser : MutableLiveData<Boolean>?= null
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
                        Toast.makeText(application!!.applicationContext,
                            "Registration Failure: ${task.exception?.message}" + task.exception?.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
    }
    fun login(email: String?, password: String?) {
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {userLiveData!!.postValue(firebaseAuth!!.currentUser)}
                    else {
                        Toast.makeText(
                            application!!.applicationContext,
                            "Login Failure: " + task.exception?.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
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



}