package com.cg.cropdeal.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.databinding.FragmentAddOnManagementBinding
import com.cg.cropdeal.model.AdminAddOnAdapter
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.AdminAddOnVM

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
    }
}