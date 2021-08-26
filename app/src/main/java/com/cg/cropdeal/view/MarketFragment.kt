package com.cg.cropdeal.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentMarketBinding
import com.cg.cropdeal.model.MarketAdapter
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.MarketVM

class MarketFragment : Fragment() {

    private lateinit var binding : FragmentMarketBinding
    private lateinit var viewModel : MarketVM
    private lateinit var progressDialog : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(MarketVM::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = UtilRepo(activity?.application!!).loadingDialog(view.context)
       progressDialog.show()
        binding.addCropsFAB.setOnClickListener {
            val bundle = bundleOf("Demo" to "First Frag")
            Navigation.findNavController(view).navigate(R.id.action_nav_market_to_crop_publish,bundle)
        }
        viewModel.getCropList()?.observe(viewLifecycleOwner,{
            Log.d("Observables","Here\t$it")
            binding.marketRview.layoutManager = LinearLayoutManager(view.context)
            if(it!=null) {
                binding.marketRview.adapter = MarketAdapter(it)
               progressDialog.dismiss()
            }
        })

    }

}