package com.cg.cropdeal.model


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

object Constants {
    const val CROPS: String = "crops"
    const val INVOICE: String = "invoice"
    const val TOPIC_PREF: String = "TOPIC_PREF"
    const val USERID: String = "userId"
    const val BANK: String = "bank"
    const val ACCOUNT:String = "account"
    const val IFSC:String = "ifsc"
    const val PAYMENT: String = "payment"
    const val DATE: String = "date"
    const val USERS:String = "users"
    const val APP_PREFERENCES: String = "AppPrefs"
    const val NO_OF_CARS:String = "noOfCars"
    const val USERNAME:String = "name"
    const val EMAIL:String = "email"

    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val FARMER: String = "farmer"
    const val DEALER:String = "dealer"

    const val IMAGE:String = "image"
    const val LOCATION_PREF :String = "LOCATION_PREF"

    var cropsList : MutableLiveData<MutableList<String>> = MutableLiveData()





    fun showImageChooser(activity: Activity){
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        activity.startActivityForResult(i, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?):String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
    fun showSnackbar(message:String,view: View){
        val snackbar = Snackbar.make(view,message, Snackbar.LENGTH_LONG)
        snackbar.duration = 4000
        snackbar.animationMode = Snackbar.ANIMATION_MODE_FADE
        snackbar.behavior = BaseTransientBottomBar.Behavior().apply { setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY) }
        snackbar.setAction("Dismiss"){
            snackbar.dismiss()
        }

        snackbar.setActionTextColor(Color.parseColor("#ffff2222"))
        snackbar.show()
    }

    var cropList = mutableListOf<String>()
    val cropListWithoutAll = listOf<String>("Tomato","Potato","Wheat","Rice","Mango","Barley"
        ,"Strawberry","Spinach","Orange","Mustard","Pumpkin","Corn")

    fun getEncryptedSharedPreference(name:String, context: Context):SharedPreferences{
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val sharedPreferences = EncryptedSharedPreferences.create(
            name,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return sharedPreferences
    }



}