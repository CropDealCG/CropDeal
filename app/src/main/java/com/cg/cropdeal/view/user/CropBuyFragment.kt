package com.cg.cropdeal.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FarmerRatingCustomDialogBinding
import com.cg.cropdeal.databinding.PayNowLayoutBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.Invoice
import com.cg.cropdeal.model.repo.UtilRepo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

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
        progressDialog = UtilRepo().loadingDialog(view.context)
        progressDialog.show()
        val farmerId = arguments?.getString("farmerId")
        val cropId = arguments?.getString("cropId")
        val cropPrice = arguments?.getString("cropPrice")
        val cropQuantity = arguments?.getInt("cropQuantity")
        val cropRate = arguments?.getInt("cropRate")
        val cropName = arguments?.getString("cropName")

        firebaseDatabase.reference.child(Constants.USERS).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(firebaseAuth.currentUser?.uid!!).child(Constants.PAYMENT).exists()){
                    binding.bankNameTV.text =
                        snapshot.child(firebaseAuth.currentUser?.uid!!).child(Constants.PAYMENT).child("bank").value.toString()
                    binding.accountNumberPaymentTV.text =
                        snapshot.child(firebaseAuth.currentUser?.uid!!).child(Constants.PAYMENT).child("account").value.toString()
                    binding.ifscPaymentTV.text =
                        snapshot.child(firebaseAuth.currentUser?.uid!!).child(Constants.PAYMENT).child("ifsc").value.toString()
                }
                if(snapshot.child(farmerId!!).exists()){
                    binding.farmerNameTV.text =
                        snapshot.child(farmerId).child(Constants.USERNAME).value.toString()
                    "${snapshot.child(farmerId).child("rating").value.toString()}(${snapshot.child(farmerId).child("noOfRating").value.toString()})".also { binding.farmerRatingPayTV.text = it }
                    binding.totalPayAmountTV.text = cropPrice
                }
                progressDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.payNowBtn.setOnClickListener {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val invoice = Invoice(cropId!!,farmerId!!,firebaseAuth.currentUser?.uid!!,dateFormat.format(Calendar.getInstance().time),timeFormat.format(Calendar.getInstance().time),cropQuantity!!.toInt(),cropRate!!.toInt(),cropPrice!!.toInt(),cropName!!)
            firebaseDatabase.reference.child(Constants.INVOICE).child(cropId).setValue(invoice)
            firebaseDatabase.reference.child(Constants.CROPS).child(cropId).removeValue()
            val dialog = MaterialAlertDialogBuilder(view.context)
            val customBinding = FarmerRatingCustomDialogBinding.inflate(layoutInflater)
            dialog.setView(customBinding.root)
            dialog.setTitle("Please Rate the farmer")
            dialog.setCancelable(false)
            var dialogBuilder = dialog.create()
            customBinding.farmerRatingBtn.setOnClickListener {
                val rating = customBinding.farmerRatingRB.rating
                if(rating>0){
                    firebaseDatabase.reference.child(Constants.USERS).child(farmerId).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val noOfRatings = snapshot.child("noOfRating").value.toString().toInt()
                            val currentRating = snapshot.child("rating").value.toString().toDouble()
                            val newRating = (currentRating*noOfRatings+rating)/(noOfRatings+1)
                            firebaseDatabase.reference.child(Constants.USERS).child(farmerId).child("noOfRating").setValue(noOfRatings+1)
                            firebaseDatabase.reference.child(Constants.USERS).child(farmerId).child("rating").setValue("%.1f".format(newRating,Locale.ENGLISH).toDouble())
                            dialogBuilder.dismiss()
                            val bundle = bundleOf("cropId" to cropId)
                            Navigation.findNavController(view).navigate(R.id.action_crop_buy_to_invoiceDetailsFragment,bundle)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
                }else{
                    Constants.showSnackbar("Please select a rating",binding.root)
                }
            }
            dialogBuilder = dialog.create()
            dialogBuilder.show()

        }
    }
}