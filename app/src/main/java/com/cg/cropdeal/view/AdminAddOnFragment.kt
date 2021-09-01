package com.cg.cropdeal.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.databinding.CustomAdminAddonDialogBinding
import com.cg.cropdeal.databinding.FragmentAddOnManagementBinding
import com.cg.cropdeal.model.AdminAddOnAdapter
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.AdminAddOnVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AdminAddOnFragment : Fragment() {
    private lateinit var binding : FragmentAddOnManagementBinding
    private lateinit var viewModel : AdminAddOnVM
    private lateinit var progressDialog : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOnManagementBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AdminAddOnVM::class.java)

        progressDialog = UtilRepo(activity?.application!!).loadingDialog(view.context)
        progressDialog.show()

        binding.addOnRView.layoutManager = LinearLayoutManager(view.context)
        viewModel.getAddOnList()?.observe(viewLifecycleOwner,{list->
            val curList : MutableList<String> = mutableListOf()
            for(crops in list)   if(crops!="All")    curList.add(crops)
            if(list.isEmpty() || list!=null){
                binding.addOnRView.adapter = AdminAddOnAdapter(curList)
                progressDialog.dismiss()
            }
        })
        binding.annOnAddCropBtn.setOnClickListener {currentView->
            val dialog = MaterialAlertDialogBuilder(currentView.context)
            val customDialogBinding = CustomAdminAddonDialogBinding.inflate(LayoutInflater.from(currentView.context))
            dialog.setView(customDialogBinding.root)
            var dialogBuilder = dialog.create()
            customDialogBinding.customAddonDialogConfirmBtn.setOnClickListener {
                val cropName = customDialogBinding.customAddonDialogName.editText?.text.toString()
                if(cropName.isNotEmpty()){
                    viewModel.addCrop(cropName)
//                    binding.addOnRView.adapter?.notifyDataSetChanged()
                    dialogBuilder.dismiss()
                    Constants.showSnackbar("Crop Added",currentView)
                }else{
                    dialogBuilder.dismiss()
                }
            }
            dialogBuilder=dialog.create()
            dialogBuilder.show()
        }
    }
}