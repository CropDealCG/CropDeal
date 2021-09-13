package com.cg.cropdeal.viewmodel

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cg.cropdeal.model.repo.AuthRepo
import com.cg.cropdeal.model.repo.UtilRepo
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser

class SignInVM(application: Application) : AndroidViewModel(application) {

    private var authRepo: AuthRepo? = null
    private var utilRepo : UtilRepo? = null
    private var userLiveData: MutableLiveData<FirebaseUser>? = null
    private var googleSignInClient : GoogleSignInClient? = null
    private var facebookCallBackManager : CallbackManager? = null
    private var signInFailed : MutableLiveData<Boolean>? = null
    private var selectedUserType : MutableLiveData<String>? = null

    init{
        authRepo = AuthRepo(application)
        userLiveData = authRepo!!.getUserLiveData()
        googleSignInClient = authRepo!!.getGoogleSignInClient()
        facebookCallBackManager = authRepo!!.getFacebookCallBackManager()
        signInFailed = authRepo!!.isSignInFailed()
        selectedUserType = authRepo!!.selectedUserType()
        utilRepo = UtilRepo()
    }
    fun login(email: String?, password: String?) {
        authRepo?.login(email, password)
    }
    /*fun getUserLiveData(): MutableLiveData<FirebaseUser>? {
        return userLiveData
    }*/
    fun getGoogleSignInClient() : GoogleSignInClient?{
        return googleSignInClient
    }
    fun getFacebookCallBackManager() : CallbackManager?{
        return  facebookCallBackManager
    }
    fun getForgotPasswordDialog(context: Context, layout: Int) : AlertDialog{
        return utilRepo?.customDialog(context,layout)!!
    }
    fun isSignInFailed():MutableLiveData<Boolean>? = signInFailed
    fun userTypeDialog(context : Context) : AlertDialog = authRepo!!.userTypeDialog(context)
    fun selectedUserType() : MutableLiveData<String>? = selectedUserType

}