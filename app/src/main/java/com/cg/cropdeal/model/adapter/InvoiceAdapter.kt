package com.cg.cropdeal.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.InvoiceListDesignBinding
import com.cg.cropdeal.model.Invoice
import com.cg.cropdeal.view.admin.AdminInvoiceFragment

class InvoiceAdapter(private val list : List<Invoice>, private val fragment: Fragment)
    : RecyclerView.Adapter<InvoiceAdapter.ViewHolder>(){

//    private lateinit var binding: InvoiceListDesignBinding

    inner class ViewHolder(var binding: InvoiceListDesignBinding) : RecyclerView.ViewHolder(binding.root){
    }

//class ViewHolder : RecyclerView.ViewHolder {
//        //binding class
//        binding = InvoiceListDesignBinding.inflate()
//        public ViewHolder(binding class) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        binding = InvoiceListDesignBinding.inflate(LayoutInflater.from(parent.context))

        return ViewHolder(InvoiceListDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val invoice = list[position]
        holder.binding.cropNameInvoiceListTV.text = invoice.cropName
        invoice.date.also { holder.binding.dateInvoiceListTV.text = it }
        holder.binding.totalInvoiceListTV.text = invoice.totalPrice.toString()
        if (fragment !is AdminInvoiceFragment) {
            holder.itemView.setOnClickListener {
                Navigation.findNavController(it).navigate(
                    R.id.action_nav_invoice_to_invoiceDetailsFragment,
                    bundleOf("cropId" to invoice.cropId)
                )
            }
        }
    }

    override fun getItemCount(): Int = list.size
}