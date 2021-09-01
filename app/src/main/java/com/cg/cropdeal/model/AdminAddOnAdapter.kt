package com.cg.cropdeal.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cg.cropdeal.databinding.AddOnLayoutBinding

class AdminAddOnAdapter(private val list: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var binding : AddOnLayoutBinding
    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAddOnAdapter.ViewHolder {
        binding = AddOnLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crop = list[position]
        binding.cropNameAddONTV.text = crop
    }

    override fun getItemCount(): Int = list.size
}