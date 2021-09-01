package com.cg.cropdeal.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
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
        setHasOptionsMenu(true)
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.invoice_filter_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.filter_by_date-> {
                filterByDate()
                true
            }
            R.id.filter_by_crop ->{
                filterByCrop()
                true
            }
            else ->
                return super.onOptionsItemSelected(item)


        }

    }

    private fun filterByCrop(){

    }

    private fun filterByDate() {
        TODO("Not yet implemented")
    }

    companion object{

        }
}