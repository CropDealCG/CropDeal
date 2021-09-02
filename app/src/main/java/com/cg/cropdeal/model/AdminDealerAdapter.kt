package com.cg.cropdeal.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.databinding.DealerManagementLayoutBinding
import com.google.firebase.database.FirebaseDatabase

class AdminDealerAdapter(private val list : List<Users>, private val userId : List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private lateinit var binding : DealerManagementLayoutBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var progressDialog : AlertDialog

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDealerAdapter.ViewHolder {
        binding = DealerManagementLayoutBinding.inflate(LayoutInflater.from(parent.context))
        firebaseDatabase = FirebaseDatabase.getInstance()
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dealer = list[position]
        binding.dealerUIDTV.text = userId[position]
        binding.dealerNameDMTV.text = dealer.name
        holder.itemView.setOnClickListener {
            //Code Here
        }
        if(dealer.active){
            binding.dealerAdminToggle.text = "Active"
            binding.dealerAdminToggle.isChecked = true
        }else{
            binding.dealerAdminToggle.text = "Inactive"
            binding.dealerAdminToggle.isChecked = false
        }

        progressDialog = UtilRepo().loadingDialog(holder.itemView.context)

        binding.dealerAdminToggle.setOnCheckedChangeListener { toggleButton, b ->
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