package com.cg.cropdeal.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FarmerManagementLayoutBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.Users
import com.cg.cropdeal.model.repo.UtilRepo
import com.google.firebase.database.FirebaseDatabase

class AdminFarmerAdapter(private val list : List<Users>, private val userId : List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var binding : FarmerManagementLayoutBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var progressDialog : AlertDialog

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = FarmerManagementLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val farmer = list[position]
        binding.farmerUIDTV.text = userId[position]
        binding.farmerAdminRatingTV.text = farmer.rating.toString()
        binding.farmerNameFMTV.text = farmer.name
        holder.itemView.setOnClickListener {
            val bundle = bundleOf(Constants.USERID to userId[position])
            Navigation.findNavController(it).navigate(
                R.id.action_adminFarmerFragment_to_adminEditProfileFragment,bundle)
        }
        if(farmer.active){
            binding.farmerAdminToggle.text = Constants.ACTIVE
            binding.farmerAdminToggle.isChecked = true
        }else{
            binding.farmerAdminToggle.text = Constants.INACTIVE
            binding.farmerAdminToggle.isChecked = false
        }

        progressDialog = UtilRepo().loadingDialog(holder.itemView.context)

        binding.farmerAdminToggle.setOnCheckedChangeListener { toggleButton, b ->
            notifyItemChanged(position)
            if(!b){
                progressDialog.show()
                toggleButton.text = Constants.INACTIVE
                firebaseDatabase.reference.child(Constants.USERS).child(userId[position])
                    .child(Constants.ACTIVE.lowercase()).setValue(false)
                progressDialog.dismiss()

            }else{
                progressDialog.show()
                toggleButton.text = Constants.ACTIVE
                firebaseDatabase.reference.child(Constants.USERS).child(userId[position])
                    .child(Constants.ACTIVE.lowercase()).setValue(true)
                progressDialog.dismiss()
            }
        }
    }

    override fun getItemCount(): Int = list.size
}