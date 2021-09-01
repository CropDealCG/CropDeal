package com.cg.cropdeal.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var subscribedTopic : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val splashScreen =  installSplashScreen()
        setContentView(binding.root)

        //Crop Subscription Notification
        val sharedPref = getSharedPreferences(Constants.TOPIC_PREF,Context.MODE_PRIVATE)
        sharedPref.registerOnSharedPreferenceChangeListener(this)
        //take single topic
        subscribedTopic = sharedPref.getString("topic","")!!
        //ChildEventListener to be notified about new child data(Crops)
        if(!subscribedTopic.isNullOrEmpty()){
            FirebaseDatabase.getInstance().reference.child("crops").addChildEventListener(object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if(snapshot.exists()){
                        //if new data is there, create a notification
                        val nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        val builder: Notification.Builder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//checks version
                            val channel = NotificationChannel("Subscriptions", "Work Done", NotificationManager.IMPORTANCE_DEFAULT)
                            nManager.createNotificationChannel(channel)
                            Notification.Builder(applicationContext, "Subscriptions")
                        } else  Notification.Builder(applicationContext)
                        //Notification bar configuration(look)

                        builder.setSmallIcon(R.drawable.logo_without_text)
                        builder.setContentTitle("$subscribedTopic is available!")
                        builder.setContentText("Buy NOW")
                        val notifyIntent = Intent(this@MainActivity,NavigationActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        val notifyPendingIntent = PendingIntent.getActivity(
                            applicationContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        builder.setContentIntent(notifyPendingIntent)
                        builder.setAutoCancel(true)
                        val myNotify =builder.build()

                        if(snapshot.child("cropName").value.toString() == subscribedTopic){
                            nManager.notify(99,myNotify)
                        }
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

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        subscribedTopic = p0?.getString("topic","")!!
    }

}