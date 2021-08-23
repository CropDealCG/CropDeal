package com.cg.cropdeal.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cg.cropdeal.databinding.PublishCropBinding
import com.cg.cropdeal.viewmodel.CropPublishVM

class CropPublishFragment : Fragment() {

    companion object {
        fun newInstance() = CropPublishFragment()
    }

    private lateinit var viewModel: CropPublishVM
    private lateinit var binding: PublishCropBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PublishCropBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CropPublishVM::class.java)
    }

}