package com.cg.cropdeal.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.cg.cropdeal.databinding.AdminMainActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.cg.cropdeal.R


const val INVOICES = "Invoices"
        const val CROPS = "Crops"
class AdminActivity : AppCompatActivity() {

    private lateinit var binding : AdminMainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

    }
}