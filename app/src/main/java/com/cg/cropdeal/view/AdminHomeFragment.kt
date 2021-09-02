package com.cg.cropdeal.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.Navigation
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentAdminHomeBinding
import com.google.firebase.auth.FirebaseAuth


class AdminHomeFragment : Fragment() {

    private lateinit var binding: FragmentAdminHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.adminEmailTV.text = FirebaseAuth.getInstance().currentUser?.email
        binding.reportManagementTV.setOnClickListener {

            startActivity(Intent(requireActivity(),AdminReportActivity::class.java))

//            val popup = PopupMenu(activity, it)
//
//            popup.menu.add(INVOICES)
//            popup.menu.add(CROPS)
//            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
//                when(item.title){
//                    INVOICES -> {
//                        Navigation.findNavController(view)
//                            .navigate(R.id.action_adminHomeFragment_to_adminInvoiceFragment)
//
//                    }
//                    CROPS -> {
//                        Navigation.findNavController(view)
//                            .navigate(R.id.action_adminHomeFragment_to_adminCropsReportFragment)
//                    }
//
//                }
//
//                true
//            })
//
//            popup.show()
        }

        binding.dealerManagementTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminDealerFragment)
        }
        binding.farmerManagementTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminFarmerFragment)
        }
        binding.addONManagementTV.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminAddOnFragment)
        }

        binding.adminLogoutTV.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity,SignInActivity::class.java))
            activity?.finish()
        }

    }
}