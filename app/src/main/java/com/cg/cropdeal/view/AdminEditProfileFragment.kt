package com.cg.cropdeal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentAdminEditProfileBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.viewmodel.AdminEditProfileVM
import java.text.SimpleDateFormat
import java.util.*


const val USER_ID = "userId"

class AdminEditProfileFragment : Fragment() {

    private lateinit var binding:FragmentAdminEditProfileBinding
    private lateinit var viewModel: AdminEditProfileVM
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString(USER_ID,"")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminEditProfileBinding.inflate(inflater,container,false)
        return binding.root
         }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminEditProfileVM::class.java)

        binding.adminEditEmailET.isEnabled = false
        val liveData = viewModel.getDataSnapshotLiveData(uid!!)
        liveData.observe(viewLifecycleOwner,
            { dataSnapshot ->
                if (dataSnapshot != null) {

                    val username =
                        dataSnapshot.child(Constants.USERNAME).value.toString()
                    binding.adminEditUserNameET.editText?.setText(username)

                    val email = dataSnapshot.child(Constants.EMAIL).value.toString()
                    binding.adminEditEmailET.editText?.setText(email)

                    val cars = dataSnapshot.child(Constants.NO_OF_CARS).value.toString()
                    binding.adminEditCarsET.editText?.setText(cars)

                    val dob = dataSnapshot.child(Constants.DATE).value.toString()
                    binding.adminEditDobTV.setText(dob)
                }
            })


        binding.adminEditDobTV.setOnClickListener{
            val datePickerDialog = viewModel.selectDate(requireContext())
            datePickerDialog.addOnPositiveButtonClickListener { dateInMillis->
                val date = SimpleDateFormat("MMM dd, yyyy",
                    Locale.getDefault()).format(Date(dateInMillis))
                binding.adminEditDobTV.text = date
            }
            datePickerDialog.show(activity?.supportFragmentManager!!,"Date")
        }

        binding.adminSaveProfileButton.setOnClickListener {
           updateUserProfileDetails()
        }
    }

    private fun updateUserProfileDetails() {
        val username = binding.adminEditUserNameET.editText?.text.toString().trim{it<=' '}
        val dob = binding.adminEditDobTV.text.toString().trim{it<=' '}
        val vehicle = binding.adminEditCarsET.editText?.text.toString()
        viewModel.updateUserProfileDetails(uid!!,username,dob,vehicle)


        Constants.showSnackbar("Profile updated successfully",
            binding.adminEditProfileLyt)

        Navigation.findNavController(requireView()).popBackStack()
    }

    companion object {

    }
}