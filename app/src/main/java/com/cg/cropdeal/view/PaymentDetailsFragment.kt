package com.cg.cropdeal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cg.cropdeal.databinding.FragmentPaymentDetailsBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.viewmodel.PaymentDetailsVM

private lateinit var binding:FragmentPaymentDetailsBinding
private lateinit var viewModel : PaymentDetailsVM
class PaymentDetailsFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PaymentDetailsVM::class.java)
        val liveData = viewModel.getDataSnapshotLiveData()
        liveData.observe(viewLifecycleOwner,{
            dataSnapshot ->
            if(dataSnapshot!=null){
                val bank = dataSnapshot.child(Constants.BANK).value.toString()
                binding.addBankNameET.editText?.setText(bank)

                val account = dataSnapshot.child(Constants.ACCOUNT).value.toString()
                binding.addAccountET.editText?.setText(account)

                val ifsc = dataSnapshot.child(Constants.IFSC).value.toString()
                binding.addIFSCET.editText?.setText(ifsc)
            }
        })
        binding.savePaymentDetailsButton.setOnClickListener {
            if (checkDetails()) {
                viewModel.uploadPaymentDetails(
                    binding.addBankNameET.editText?.text.toString(),
                    binding.addAccountET.editText?.text.toString().toLong(),
                    binding.addIFSCET.editText?.text.toString()
                )
            }
            Constants.showSnackbar("Updated successfully!", binding.paymentDetailsLyt)
            Navigation.findNavController(view).popBackStack()
        }
    }

    fun checkDetails():Boolean{
        if(binding.addAccountET.editText?.text.isNullOrEmpty() ||
                binding.addBankNameET.editText?.text.isNullOrEmpty() ||
                binding.addIFSCET.editText?.text.isNullOrEmpty()
        ) {
            Constants.showSnackbar(
                "Please enter all the details",
                binding.paymentDetailsLyt
            )
            return false
        }
        return true

    }
}