package com.cg.cropdeal.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.InvoiceLayoutBinding
import com.cg.cropdeal.model.UtilRepo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InvoiceDetailsFragment : Fragment() {
    private lateinit var binding : InvoiceLayoutBinding
    private lateinit var progressBar : AlertDialog
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InvoiceLayoutBinding.inflate(inflater,container,false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = UtilRepo(activity?.application!!).loadingDialog(view.context)
        progressBar.show()
        val cropId = arguments?.getString("cropId")
        val databaseReference = firebaseDatabase.reference
        databaseReference.child("invoice").child(cropId!!).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(mainSnapshot: DataSnapshot) {
                if(mainSnapshot.exists()){
                    databaseReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                binding.sellerNameInvoiceTV.text = snapshot.child(mainSnapshot.child("farmerId").value.toString()).child("name").value.toString()
                                binding.buyerNameInvoiceTV.text  = snapshot.child(mainSnapshot.child("buyerId").value.toString()).child("name").value.toString()
                                progressBar.hide()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
                    binding.transactionIDTV.text = mainSnapshot.child("cropId").value.toString()
                    "${mainSnapshot.child("time").value.toString()} ${mainSnapshot.child("date").value.toString()}".also { binding.transactionDateTV.text = it }
                    binding.cropInvoiceNameTV.text = mainSnapshot.child("cropName").value.toString()
                    binding.quantityInvoiceTV.text = mainSnapshot.child("quantity").value.toString()
                    binding.rateInvoiceTV.text     = mainSnapshot.child("rate").value.toString()
                    binding.totalInvoiceTV.text    = mainSnapshot.child("totalPrice").value.toString()
                }
                binding.okayInvoiceBtn.setOnClickListener {
                    Navigation.findNavController(view).popBackStack(R.id.nav_market,false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


}


