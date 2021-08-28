package com.cg.cropdeal.model


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import com.google.android.material.snackbar.Snackbar

object Constants {
    const val TOPIC_PREF: String = "TOPIC_PREF"
    const val USERID: String = "userId"
    const val BANK: String = "bank"
    const val ACCOUNT:String = "account"
    const val IFSC:String = "ifsc"
    const val PAYMENT: String = "payment"
    const val DATE: String = "date"
    const val USERS:String = "users"
    const val APP_PREFERENCES: String = "AppPrefs"

    const val USERNAME:String = "name"
    const val EMAIL:String = "email"

    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val FARMER: String = "farmer"
    const val DEALER:String = "dealer"

    const val IMAGE:String = "image"
    const val LOCATION_PREF :String = "LOCATION_PREF"





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
        snackbar.duration
        snackbar.show()
    }

    val cropList = listOf<String>("Tomato","Potato","Wheat","Rice","Mango","Barley"
    ,"Strawberry","Spinach","Orange","Mustard","Pumpkin","Corn")

}