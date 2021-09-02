package com.cg.cropdeal.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.databinding.AddOnLayoutBinding
import com.cg.cropdeal.databinding.CustomAdminAddonDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class AdminAddOnAdapter(private val list: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var binding : AddOnLayoutBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAddOnAdapter.ViewHolder {
        binding = AddOnLayoutBinding.inflate(LayoutInflater.from(parent.context))
        firebaseDatabase = FirebaseDatabase.getInstance()
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crop = list[position]
        binding.cropNameAddONTV.text = crop
        binding.addOnCropDeleteBtn.setOnClickListener {
            firebaseDatabase.reference.child("cropList").child("-MiVmI45YZTkIdxU1UB6").child(crop).removeValue()
            Constants.showSnackbar("Crop Deleted",holder.itemView)
            notifyItemRemoved(position)
        }
        binding.addOnCropEditBtn.setOnClickListener {currentView->
            val dialog = MaterialAlertDialogBuilder(currentView.context)
            val customDialogBinding = CustomAdminAddonDialogBinding.inflate(LayoutInflater.from(currentView.context))
            dialog.setView(customDialogBinding.root)
            var dialogBuilder = dialog.create()
            customDialogBinding.customAddonDialogName.editText?.setText(crop)
            customDialogBinding.customAddonDialogConfirmBtn.setOnClickListener {
                val cropName = customDialogBinding.customAddonDialogName.editText?.text.toString()
                if(cropName.isNotEmpty()){
                    firebaseDatabase.reference.child("cropList").child("-MiVmI45YZTkIdxU1UB6")
                        .child(crop).removeValue()
                    firebaseDatabase.reference.child("cropList").child("-MiVmI45YZTkIdxU1UB6")
                        .child(cropName).setValue(0)
                    notifyItemChanged(position)
                    dialogBuilder.dismiss()
                    Constants.showSnackbar("Crop Updated",currentView)
                }else{
                    dialogBuilder.dismiss()
                }
            }
            dialogBuilder=dialog.create()
            dialogBuilder.show()
        }
    }

    override fun getItemCount(): Int = list.size
}