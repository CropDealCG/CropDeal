package com.cg.cropdeal.model


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS:String = "users"
    const val APP_PREFERENCES: String = "AppPrefs"

    const val USERNAME:String = "name"


    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1


    const val IMAGE:String = "image"
    const val LOCATION_PREF :String = "LOCATION_PREF"


    const val PROFILE_IMAGE_REF:String = "PROFILE_IMAGE_REF"


    fun showImageChooser(activity: Activity){
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        activity.startActivityForResult(i, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?):String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }


}