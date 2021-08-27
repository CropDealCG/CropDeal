package com.cg.cropdeal.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.cg.cropdeal.databinding.PayNowLayoutBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.UtilRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CropBuyFragment : Fragment() {
    private lateinit var binding : PayNowLayoutBinding
    private lateinit var progressDialog : AlertDialog
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PayNowLayoutBinding.inflate(inflater,container,false)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = UtilRepo(activity?.application!!).loadingDialog(view.context)
        progressDialog.show()
        val farmerId = arguments?.getString("farmerId")
        val cropPrice = arguments?.getString("cropPrice")
        firebaseDatabase.reference.child("users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(firebaseAuth?.currentUser?.uid!!).child(Constants.PAYMENT).exists()){
                    binding.bankNameTV.text =
                        snapshot.child(firebaseAuth?.currentUser?.uid!!).child(Constants.PAYMENT).child("bank").value.toString()
                    binding.accountNumberPaymentTV.text =
                        snapshot.child(firebaseAuth?.currentUser?.uid!!).child(Constants.PAYMENT).child("account").value.toString()
                    binding.ifscPaymentTV.text =
                        snapshot.child(firebaseAuth?.currentUser?.uid!!).child(Constants.PAYMENT).child("ifsc").value.toString()
                }
                if(snapshot.child(farmerId!!).exists()){
                    binding.farmerNameTV.text =
                        snapshot.child(farmerId!!).child("name").value.toString()
                    binding.totalPayAmountTV.text = cropPrice
                }
                progressDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}