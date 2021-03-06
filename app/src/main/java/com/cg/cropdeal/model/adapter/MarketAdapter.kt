package com.cg.cropdeal.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.MarketPostDesignLayoutBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.Crops
import com.google.firebase.database.FirebaseDatabase

class MarketAdapter(private val list: List<Crops>, private val areBankDetailsAvailable:Boolean)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: MarketPostDesignLayoutBinding
    private lateinit var fDatabase : FirebaseDatabase
    private var userType = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = MarketPostDesignLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        fDatabase = FirebaseDatabase.getInstance()
        userType = parent.context.getSharedPreferences(Constants.LOGINPREF, Context.MODE_PRIVATE)
            ?.getString(Constants.USERTYPE,"")!!
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crop = list[position]
        if(userType==Constants.FARMER)  binding.buyBtn.visibility = View.INVISIBLE
        binding.cropNameTV.text = crop.cropName
        var address = crop.cropLocation
        if(address.length>22)
            address = address.substring(0,22) + ".."
        binding.addressTV.text = address
        binding.cropTypeTV.text = crop.cropType
        var cropDesc = crop.cropDesc
        if(cropDesc.length>20)
            cropDesc = cropDesc.substring(0,20) + ".."
        binding.descriptionMarketTV.text = cropDesc
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
                dialog.setTitle(it.context.getString(R.string.noBankDialogTitle))
                dialog.setMessage(it.context.getString(R.string.noBankDialogMessage))
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