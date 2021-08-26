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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MarketAdapter(private val list:List<Crops>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: MarketPostDesignLayoutBinding
    private lateinit var fDatabase : FirebaseDatabase
    private lateinit var sellerName : MutableLiveData<String>
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
        sellerName = MutableLiveData<String>("Anonymous")

        fDatabase.reference.child("users").child(crop.userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    CoroutineScope(Dispatchers.Main).launch {
                        val name = CoroutineScope(Dispatchers.Default).async {
                            snapshot.child("name").value.toString()
                        }
                        binding.sellerNameTV.text = name.await()
                    }
//                    sellerName.postValue(snapshot.child("name").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        sellerName.observe(ProcessLifecycleOwner.get(),{
            Log.d("Observabless","$it")
            binding.sellerNameTV.text = it

        })

    }

    override fun getItemCount(): Int = list.size
    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
    }
}