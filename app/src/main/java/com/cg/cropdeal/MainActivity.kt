package com.cg.cropdeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cg.cropdeal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.mainTv.text = "Demo"
        setContentView(binding.root)
    }
}