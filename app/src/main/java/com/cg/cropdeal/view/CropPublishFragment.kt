package com.cg.cropdeal.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.cg.cropdeal.databinding.PublishCropBinding
import com.cg.cropdeal.model.Crops
import com.cg.cropdeal.viewmodel.CropPublishVM
import com.google.firebase.auth.FirebaseAuth

class CropPublishFragment : Fragment() {

    private lateinit var viewModel: CropPublishVM
    private lateinit var binding: PublishCropBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PublishCropBinding.inflate(inflater,container,false)
        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CropPublishVM::class.java)
        val spinnerList = listOf<String>("Spinach","Tomato")
        val spinnerAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_dropdown_item,spinnerList) as SpinnerAdapter
        binding.cropname.adapter = spinnerAdapter
        binding.publishBtn.setOnClickListener {

            val cropName = binding.cropname.selectedItem.toString()
            val cropDesc = binding.cropDescriptionET.editText?.text.toString()
            val cropQuantity = binding.quantityET.editText?.text.toString().toInt()
            val cropRate = binding.rateET.editText?.text.toString().toInt()
            val address = binding.addressET.editText?.text.toString()
            val userId = firebaseAuth.currentUser?.uid
            var cropType = "Fruit"
            binding.radioGroup.setOnCheckedChangeListener { _, i ->
                when(i){
                    binding.fruitRadio.id -> cropType = "Fruit"
                    binding.vegRadio.id -> cropType = "Vegetable"
                }
            }
            viewModel.addCrops(Crops(cropName,cropType,cropQuantity,cropRate,address,cropDesc,userId!!))
        }
    }

}