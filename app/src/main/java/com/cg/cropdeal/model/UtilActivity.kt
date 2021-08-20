package com.cg.cropdeal.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar

open class UtilActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    fun showSnackbar(message:String,view: View){
        val snackbar = Snackbar.make(view,message, Snackbar.LENGTH_LONG)

        snackbar.show()
    }
}