package com.cg.cropdeal.view


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.cg.cropdeal.databinding.PublishCropBinding
import com.cg.cropdeal.model.Crops
import com.cg.cropdeal.model.UtilActivity
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.CropPublishVM
import com.google.firebase.auth.FirebaseAuth

class CropPublishFragment : Fragment() {

    private lateinit var viewModel: CropPublishVM
    private lateinit var binding: PublishCropBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : AlertDialog


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


        progressDialog = UtilRepo(activity?.application!!).loadingDialog(view.context)

        val a = arguments?.get("Demo")
        Toast.makeText(view.context,"$a",Toast.LENGTH_LONG).show()

        viewModel = ViewModelProvider(this).get(CropPublishVM::class.java)
        val spinnerList = listOf<String>("Spinach","Tomato")
        val spinnerAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_dropdown_item,spinnerList) as SpinnerAdapter
        binding.cropname.adapter = spinnerAdapter
        var cropType = "Fruit"
        binding.radioGroup.setOnCheckedChangeListener { _, i ->
            Log.d("Observabless","Radio")
            when(i){
                binding.fruitRadio.id -> cropType = "Fruit"
                binding.vegRadio.id -> cropType = "Vegetable"
            }
        }
        binding.radioGroup.check(binding.vegRadio.id)
        binding.publishBtn.setOnClickListener {
            val cropName = binding.cropname.selectedItem.toString()
            val cropDesc = binding.cropDescriptionET.editText?.text.toString()
            val cropQuantity = binding.quantityET.editText?.text.toString()
            val cropRate = binding.rateET.editText?.text.toString()
            val address = binding.addressET.editText?.text.toString()
            val userId = firebaseAuth.currentUser?.uid
            if(cropName.isEmpty() || cropDesc.isEmpty() || address.isEmpty() || binding.quantityET.editText?.text.toString().isEmpty() || binding.rateET.editText?.text.toString().isEmpty()){
                UtilActivity().showSnackbar("Please Enter all the details",view)
            }else{
                progressDialog.show()

                val uuid = viewModel.returnUUID()
                val crop = Crops(uuid,cropName,cropType,cropQuantity.toInt(),cropRate.toInt(),address,cropDesc,userId!!)
                viewModel.returnCrop()?.postValue(crop)
                viewModel.addCrops(crop,uuid)

                viewModel.isCropAdded()?.observe(viewLifecycleOwner,{
                    if(it!=null && it==true) {
                        progressDialog.dismiss()
                        Navigation.findNavController(view).popBackStack()
                    }

                })
            }



        }
    }


}