package com.cg.cropdeal.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.ActivityMainBinding
import com.cg.cropdeal.model.Constants
import com.facebook.FacebookSdk
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val splashScreen =  installSplashScreen()
        setContentView(binding.root)
        val subscribedTopic = getSharedPreferences(Constants.TOPIC_PREF,Context.MODE_PRIVATE).getString("topic","")
        if(!subscribedTopic.isNullOrEmpty()){
            FirebaseDatabase.getInstance().reference.child("crops").addChildEventListener(object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if(snapshot.exists()){
                        var notificationBuilder : NotificationCompat.Builder
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                            val channel = NotificationChannel("Subscriptions","notification",NotificationManager.IMPORTANCE_DEFAULT)
                            notificationBuilder = NotificationCompat.Builder(applicationContext,"Subscriptions")
                        }
                        else{
                            notificationBuilder = NotificationCompat.Builder(applicationContext,"Subscriptions")
                        }
                        Toast.makeText(applicationContext,"Database changed",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
        FirebaseDatabase.getInstance().reference.child("crops").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(this)
        startActivity(Intent(this,SignUpActivity::class.java))
        finish()
    }

}