package com.cg.cropdeal.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import com.cg.cropdeal.databinding.AdminMainActivityBinding
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.cg.cropdeal.R


const val INVOICES = "Invoices"
        const val CROPS = "Crops"
class AdminActivity : AppCompatActivity() {

    private lateinit var binding : AdminMainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseAuth.getInstance()

        binding.reportManagementTV.setOnClickListener {
            val popup = PopupMenu(this, it)

            popup.menu.add(INVOICES)
            popup.menu.add(CROPS)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.title){
                    INVOICES -> {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.container,AdminInvoiceFragment(),
                                "AdminInvoiceFragment")
                            .addToBackStack(null)
                            .commit()
                    }
                    CROPS -> {}

                }

                true
            })

            popup.show()
        }
    }
}