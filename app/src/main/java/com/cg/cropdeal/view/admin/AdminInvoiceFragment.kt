package com.cg.cropdeal.view.admin

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentAdminInvoiceBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.adapter.InvoiceAdapter
import com.cg.cropdeal.model.repo.UtilRepo
import com.cg.cropdeal.viewmodel.admin.AdminReportVM
import java.text.SimpleDateFormat
import java.util.*

class AdminInvoiceFragment : Fragment() {
    private lateinit var binding:FragmentAdminInvoiceBinding
    private lateinit var viewModel: AdminReportVM
    private lateinit var progressDialog : AlertDialog
    private var cropForFilter = "All" //default


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = FragmentAdminInvoiceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AdminReportVM::class.java)
        progressDialog = UtilRepo().loadingDialog(view.context)
        progressDialog.show()


        binding.adminInvoiceRview.layoutManager = LinearLayoutManager(view.context)
        viewModel.getInvoice()?.observe(viewLifecycleOwner, { list ->
            Log.d("invoiceList",list.toString())
            if (list.isEmpty() || list != null) {
                binding.adminInvoiceRview.adapter = InvoiceAdapter(list,this)
                progressDialog.dismiss()
            }
        })



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.admin_filter_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.filter_by_crop ->{
                filterInvoiceByCrop()
                true
            }
            R.id.filter_by_date -> {
                filterInvoiceByDate()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun filterInvoiceByDate() {
            val datePickerDialog = viewModel.selectDate()
        datePickerDialog.addOnPositiveButtonClickListener { dateInMillis->
            val date = SimpleDateFormat("dd/MM/yyyy",
                Locale.getDefault()).format(Date(dateInMillis))
            var invoiceFilter = "Invoices for "
            invoiceFilter += date
            binding.invoiceFilterResultTV.text =  invoiceFilter
           val list = viewModel.getInvoiceListByDate(date)
            binding.adminInvoiceRview.adapter = InvoiceAdapter(list,this)
        }
        datePickerDialog.show(activity?.supportFragmentManager!!,"Date")
    }

    private fun filterInvoiceByCrop(): Boolean {
        val builder = AlertDialog.Builder(requireContext())
        Constants.cropsList.observe(viewLifecycleOwner, {

            builder.setTitle("Select any crop")
                .setItems(it.toTypedArray(),
                    DialogInterface.OnClickListener { _, which ->
                        cropForFilter = it[which]
                        Log.d("cropFilter", cropForFilter)
                        var invoiceFilter = "Invoices for "
                        invoiceFilter += cropForFilter
                        binding.invoiceFilterResultTV.text = invoiceFilter
                        val list = viewModel.getInvoiceListByCrop(cropForFilter)
                        binding.adminInvoiceRview.adapter = InvoiceAdapter(list,this)
                    })
        })
            builder.create().show()


        return true
    }




}