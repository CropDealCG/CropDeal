package com.cg.cropdeal.model

import android.app.Application
import androidx.lifecycle.MutableLiveData

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import android.widget.Toast

class AuthRepo(private var application: Application?) {

    private var firebaseAuth: FirebaseAuth? = null
    private var userLiveData: MutableLiveData<FirebaseUser>? = null
    private var loggedOutLiveData: MutableLiveData<Boolean>? = null

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        userLiveData = MutableLiveData()
        loggedOutLiveData = MutableLiveData()
        if (firebaseAuth!!.currentUser != null) {
            userLiveData!!.postValue(firebaseAuth!!.currentUser)
            loggedOutLiveData!!.postValue(false)
        }
    }

    fun register(email: String?, password: String?) {
        firebaseAuth!!.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        userLiveData!!.postValue(firebaseAuth!!.currentUser)
                    } else {
                        Toast.makeText(application!!.applicationContext,
                            "Registration Failure: " + task.exception?.message,
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
}