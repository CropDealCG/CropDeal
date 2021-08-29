package com.cg.cropdeal.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.MarketPostDesignLayoutBinding
import com.google.firebase.database.FirebaseDatabase

class MarketAdapter(private val list:List<Crops>,private val areBankDetailsAvailable:Boolean)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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


//        fDatabase.reference.child("users").child(crop.userId).addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()){
//                    val farmerName = snapshot.child("name").value.toString()
//                    binding.sellerNameTV.text = farmerName
//                    Log.d("Observable2","$farmerName - $position")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        })
        binding.buyBtn.setOnClickListener {
            if(areBankDetailsAvailable)
            {
                val bundle = bundleOf("farmerId" to crop.userId,
                    "cropPrice" to (crop.cropQuantity*crop.cropPrice).toString(),
                    "cropId" to crop.cropId,"cropQuantity" to crop.cropQuantity,
                    "cropRate" to crop.cropPrice,"cropName" to crop.cropName)
                Navigation.findNavController(it).navigate(R.id.action_nav_market_to_crop_buy,bundle)
            }
            else{
                val dialog = AlertDialog.Builder(it.context)
                dialog.setTitle("You cannot Add new Crops without adding your bank account details")
                dialog.setMessage("Do you want to add now?")
                var dialogBuilder = dialog.create()
                dialog.setPositiveButton("OK") { _, _ ->
                    Navigation.findNavController(it).navigate(R.id.action_nav_market_to_paymentDetailsFragment)
                }
                dialog.setNegativeButton("No"){_,_->
                    dialogBuilder.dismiss()
                }
                dialogBuilder = dialog.create()
                dialogBuilder.setCancelable(false)
                dialogBuilder.show()
            }
        }

    }

    override fun getItemCount(): Int = list.size
    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}