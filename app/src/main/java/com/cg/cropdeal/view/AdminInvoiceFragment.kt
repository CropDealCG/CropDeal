package com.cg.cropdeal.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentAdminInvoiceBinding
import com.cg.cropdeal.model.InvoiceAdapter
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.AdminReportVM


class AdminInvoiceFragment : Fragment() {
    private lateinit var binding:FragmentAdminInvoiceBinding
    private lateinit var viewModel:AdminReportVM
    private lateinit var progressDialog : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminInvoiceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AdminReportVM::class.java)
        progressDialog = UtilRepo(activity?.application!!).loadingDialog(view.context)
        progressDialog.show()


        binding.adminInvoiceRview.layoutManager = LinearLayoutManager(view.context)
        viewModel.getInvoice()?.observe(viewLifecycleOwner, { list ->
            Log.d("invoiceList",list.toString())
            if (list.isEmpty() || list != null) {
                binding.adminInvoiceRview.adapter = InvoiceAdapter(list)
                progressDialog.dismiss()
            }
        })

    }

        companion object{

        }
}