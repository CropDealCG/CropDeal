package com.cg.cropdeal.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.InvoiceListDesignBinding

class InvoiceAdapter(private val list : List<Invoice>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var binding: InvoiceListDesignBinding
    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceAdapter.ViewHolder {
        binding = InvoiceListDesignBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val invoice = list[position]
        binding.cropNameInvoiceListTV.text = invoice.cropName
        "${invoice.time} ${invoice.date}".also { binding.dateInvoiceListTV.text = it }
        binding.totalInvoiceListTV.text = invoice.totalPrice.toString()
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_invoice_to_invoiceDetailsFragment,
                bundleOf("cropId" to invoice.cropId))
        }
    }

    override fun getItemCount(): Int = list.size
}