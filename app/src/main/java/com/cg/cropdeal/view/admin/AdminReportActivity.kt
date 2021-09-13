package com.cg.cropdeal.view.admin

import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.AdminReportActivityNavigationBinding

class AdminReportActivity : AppCompatActivity() {
    private lateinit var binding : AdminReportActivityNavigationBinding
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminReportActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.bottom_nav_fragment_admin)
        setupActionBarWithNavController(navController)
        setupSmoothBottomMenu()
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this,null)
        popupMenu.inflate(R.menu.admin_report_menu)
        binding.bottomBarAdmin.setupWithNavController(popupMenu.menu,navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}