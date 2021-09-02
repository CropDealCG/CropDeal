package com.cg.cropdeal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentInvoiceBinding
import com.cg.cropdeal.model.InvoiceAdapter
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.InvoiceVM
import com.cg.cropdeal.viewmodel.MarketVM


class InvoiceFragment : Fragment() {

    private lateinit var binding: FragmentInvoiceBinding
    private lateinit var viewModel : InvoiceVM
    private lateinit var progressDialog : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInvoiceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(InvoiceVM::class.java)
        progressDialog = UtilRepo().loadingDialog(view.context)
        progressDialog.show()

        binding.invoiceRview.layoutManager = LinearLayoutManager(view.context)
        viewModel.getInvoice()?.observe(viewLifecycleOwner,{list->
            if(list.isEmpty() || list!=null){
                binding.invoiceRview.adapter = InvoiceAdapter(list,this)
                progressDialog.dismiss()
            }
        })
    }


}