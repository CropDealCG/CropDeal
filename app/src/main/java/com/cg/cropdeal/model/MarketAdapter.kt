package com.cg.cropdeal.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.databinding.MarketPostDesignLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context

class MarketAdapter(private val list:List<Crops>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: MarketPostDesignLayoutBinding
    private lateinit var fDatabase : FirebaseDatabase
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketAdapter.ViewHolder {
        binding = MarketPostDesignLayoutBinding.inflate(LayoutInflater.from(parent.context))
        fDatabase = FirebaseDatabase.getInstance()
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crop = list[position]
        binding.cropNameTV.text = crop.cropName
        binding.addressTV.text = crop.cropLocation
        binding.cropTypeTV.text = crop.cropType
        binding.descriptionMarketTV.text = crop.cropDesc
        binding.quantityTV.text = crop.cropQuantity.toString()
        binding.rateTV.text = (crop.cropQuantity*crop.cropPrice).toString()
        var sellerName = MutableLiveData<String>("Anonymous")
        sellerName.observe(ProcessLifecycleOwner.get(),{
            Log.d("Observabless","$it")
            binding.sellerNameTV.text = it

        })
        fDatabase.reference.child("users").child(crop.userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    sellerName.postValue(snapshot.child("name").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    override fun getItemCount(): Int = list.size
    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
    }
}