package com.cg.cropdeal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentMarketBinding

class MarketFragment : Fragment() {

    private lateinit var binding : FragmentMarketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addCropsFAB.setOnClickListener {
            val bundle = Bundle().apply { putString("Demo","First Frag") }
            Navigation.findNavController(view).navigate(R.id.action_nav_market_to_crop_publish,bundle)
        }
    }

}