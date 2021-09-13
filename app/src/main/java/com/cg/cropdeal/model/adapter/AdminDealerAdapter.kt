package com.cg.cropdeal.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.DealerManagementLayoutBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.Users
import com.cg.cropdeal.model.repo.UtilRepo
import com.google.firebase.database.FirebaseDatabase

class AdminDealerAdapter(private val list : List<Users>, private val userId : List<String>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private lateinit var binding : DealerManagementLayoutBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var progressDialog : AlertDialog

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DealerManagementLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dealer = list[position]
        binding.dealerUIDTV.text = userId[position]
        binding.dealerNameDMTV.text = dealer.name
        holder.itemView.setOnClickListener {
            val bundle = bundleOf(Constants.USERID to userId[position])
            Navigation.findNavController(it).navigate(
                R.id.action_adminDealerFragment_to_adminEditProfileFragment,bundle)
        }
        if(dealer.active){
            binding.dealerAdminToggle.text = Constants.ACTIVE
            binding.dealerAdminToggle.isChecked = true
        }else{
            binding.dealerAdminToggle.text = Constants.INACTIVE
            binding.dealerAdminToggle.isChecked = false
        }

        progressDialog = UtilRepo().loadingDialog(holder.itemView.context)

        binding.dealerAdminToggle.setOnCheckedChangeListener { toggleButton, b ->
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