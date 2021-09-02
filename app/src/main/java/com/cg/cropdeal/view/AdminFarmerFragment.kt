package com.cg.cropdeal.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.databinding.FragmentFarmerManagementBinding
import com.cg.cropdeal.model.AdminDealerAdapter
import com.cg.cropdeal.model.AdminFarmerAdapter
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.AdminFarmerVM

class AdminFarmerFragment : Fragment() {
    private lateinit var binding : FragmentFarmerManagementBinding
    private lateinit var viewModel : AdminFarmerVM
    private lateinit var progressDialog : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFarmerManagementBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminFarmerVM::class.java)

        progressDialog = UtilRepo().loadingDialog(view.context)
        progressDialog.show()

        binding.adminFarmerRview.layoutManager = LinearLayoutManager(view.context)
        viewModel.getFarmerIdList()?.observe(viewLifecycleOwner,{ idList->
            viewModel.getFarmerList()?.observe(viewLifecycleOwner,{list->
                if(idList.isEmpty() || idList!=null){
                    if(list.isEmpty() || list!=null){
                        binding.adminFarmerRview.adapter = AdminDealerAdapter(list,idList)
                        progressDialog.dismiss()
                    }
                }
            })
        })
    }
}