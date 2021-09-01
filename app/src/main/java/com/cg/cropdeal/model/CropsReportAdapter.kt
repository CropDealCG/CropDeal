package com.cg.cropdeal.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.databinding.CropsPostDesignLayoutBinding
import com.google.firebase.database.FirebaseDatabase

class CropsReportAdapter(private val list:List<Crops>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        private lateinit var binding:CropsPostDesignLayoutBinding
        private lateinit var fDatabase:FirebaseDatabase
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = CropsPostDesignLayoutBinding.inflate(LayoutInflater.from(parent.context))
        fDatabase = FirebaseDatabase.getInstance()
        return MarketAdapter.ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crop = list[position]
        binding.cropNameTV.text = crop.cropName
        binding.addressTV.text = crop.cropLocation
        binding.cropTypeTV.text = crop.cropType
        binding.descriptionMarketTV.text = crop.cropDesc
        binding.quantityTV.text = crop.cropQuantity.toString()
        binding.rateTV.text = (crop.cropQuantity*crop.cropPrice).toString()
    }

    override fun getItemCount(): Int = list.size
    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}