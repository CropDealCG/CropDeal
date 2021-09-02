package com.cg.cropdeal.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.databinding.FarmerManagementLayoutBinding
import com.google.firebase.database.FirebaseDatabase

class AdminFarmerAdapter(private val list : List<Users>, private val userId : List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var binding : FarmerManagementLayoutBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var progressDialog : AlertDialog

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminFarmerAdapter.ViewHolder {
        binding = FarmerManagementLayoutBinding.inflate(LayoutInflater.from(parent.context))
        firebaseDatabase = FirebaseDatabase.getInstance()
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val farmer = list[position]
        binding.farmerUIDTV.text = userId[position]
        binding.farmerAdminRatingTV.text = farmer.rating.toString()
        binding.farmerNameFMTV.text = farmer.name
        holder.itemView.setOnClickListener {
            // Code Here
        }
        Log.d("Observables",list.toString())
        if(farmer.active){
            binding.farmerAdminToggle.text = "Active"
            binding.farmerAdminToggle.isChecked = true
        }else{
            binding.farmerAdminToggle.text = "Inactive"
            binding.farmerAdminToggle.isChecked = false
        }
        progressDialog = UtilRepo().loadingDialog(holder.itemView.context)
        binding.farmerAdminToggle.setOnCheckedChangeListener { toggleButton, b ->
            notifyItemChanged(position)
            if(!b){
                progressDialog.show()
                toggleButton.text = "Inactive"
                firebaseDatabase.reference.child("users").child(userId[position])
                    .child("active").setValue(false)
                progressDialog.dismiss()

            }else{
                progressDialog.show()
                toggleButton.text = "Active"
                firebaseDatabase.reference.child("users").child(userId[position])
                    .child("active").setValue(true)
                progressDialog.dismiss()
            }
        }
    }

    override fun getItemCount(): Int = list.size
}