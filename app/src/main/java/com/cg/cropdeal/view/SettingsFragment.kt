package com.cg.cropdeal.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.LogoutDialogBinding
import com.cg.cropdeal.databinding.SettingsFragmentBinding
import com.cg.cropdeal.model.Constants

import com.cg.cropdeal.viewmodel.SettingsVM

import com.google.firebase.auth.FirebaseAuth

import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

import androidx.core.graphics.drawable.RoundedBitmapDrawable

import android.graphics.Bitmap
import com.bumptech.glide.request.RequestOptions

import com.bumptech.glide.request.target.BitmapImageViewTarget




class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsVM
    private var _binding : SettingsFragmentBinding? = null


    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.settings_fragment, container, false)
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uri ="img"+FirebaseAuth.getInstance().currentUser?.email


        Glide.with(this )
            .load(uri)
            .placeholder(R.drawable.blank_profile)
            .circleCrop()
            .into(binding.profileUserImage)

//        Glide.with(this).load(uri).asBitmap().centerCrop()
//            .into(object : BitmapImageViewTarget(binding.profileUserImage) {
//                override fun setResource(resource: Bitmap?) {
//                    val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
//                        context!!.resources, resource
//                    )
//                    circularBitmapDrawable.isCircular = true
//                    binding.profileUserImage.setImageDrawable(circularBitmapDrawable)
//                }
//            })


        viewModel = ViewModelProvider(this).get(SettingsVM::class.java)
        val liveData = viewModel.getDataSnapshotLiveData()
        liveData.observe(viewLifecycleOwner,
            { dataSnapshot ->
                if (dataSnapshot != null) {

                    val username =
                        dataSnapshot.child(Constants.USERNAME).value.toString()
                    binding.profileUserName.setText(username)

                    val email = dataSnapshot.child(Constants.EMAIL).value.toString()
                    binding.profileUserEmail.setText(email)
                }
            })


        binding.editProfTV.setOnClickListener {

                val intent = Intent(activity, EditProfileActivity::class.java)

                startActivity(intent)

        }

        binding.changePassTV.setOnClickListener {
            val intent = Intent(activity,ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.paymentDetailsTV.setOnClickListener{

            Navigation.findNavController(view)
                .navigate(R.id.action_nav_setting_to_paymentDetailsFragment)
        }

        binding.aboutUsTV.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_nav_setting_to_aboutUsFragment)
        }

        binding.logoutTV.setOnClickListener {
            val dialog = viewModel.getLogoutDialog(it.context,R.layout.logout_dialog)
            val layoutInflater = LayoutInflater.from(context)
            val logoutBinding = LogoutDialogBinding.inflate(layoutInflater)
             dialog.setView(logoutBinding.root)

            logoutBinding.settingsExitButton.setOnClickListener {
                activity?.finish()
            }
            logoutBinding.settingsLogoutButton.setOnClickListener{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(activity, SignInActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            dialog.show()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}