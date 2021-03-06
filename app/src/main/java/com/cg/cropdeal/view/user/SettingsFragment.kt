package com.cg.cropdeal.view.user

import android.content.Context
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

import com.cg.cropdeal.viewmodel.user.SettingsVM

import com.google.firebase.auth.FirebaseAuth

import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

import android.graphics.BitmapFactory
import androidx.core.content.edit
import com.cg.cropdeal.view.SignInActivity
import com.facebook.login.LoginManager
import kotlin.math.max


class SettingsFragment : Fragment() {


    private lateinit var viewModel: SettingsVM
    private var userType = ""
    private var _binding : SettingsFragmentBinding? = null


    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.settings_fragment, container, false)
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        userType = activity?.getSharedPreferences(Constants.LOGINPREF,Context.MODE_PRIVATE)?.getString(Constants.USERTYPE,"")!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(userType==Constants.FARMER)  _binding?.subsciptionLayout?.visibility = View.GONE
        val profileImageRef = activity?.getSharedPreferences(
            FirebaseAuth.getInstance().currentUser?.email,0)
        val uri = profileImageRef?.getString("profile_image","")

        val src = BitmapFactory.decodeResource(resources, R.drawable.blank_profile)
        val dr = RoundedBitmapDrawableFactory.create(resources, src)
        dr.cornerRadius = max(src.width, src.height) / 2.0f
        //binding.profileUserImage.setImageDrawable(dr)

        Glide.with(this )
            .load(uri)
            .placeholder(dr)
            .circleCrop()
            .into(binding.profileUserImage)


        viewModel = ViewModelProvider(this).get(SettingsVM::class.java)
        val liveData = viewModel.getDataSnapshotLiveData()
        liveData.observe(viewLifecycleOwner,
            { dataSnapshot ->
                if (dataSnapshot != null) {

                    val username =
                        dataSnapshot.child(Constants.USERNAME).value.toString()
                    binding.profileUserName.text = username

                    val email = dataSnapshot.child(Constants.EMAIL).value.toString()
                    binding.profileUserEmail.text = email
                }
            })


        binding.editProfTV.setOnClickListener {

                val intent = Intent(activity, EditProfileActivity::class.java)

                startActivity(intent)

        }

        binding.changePassTV.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_nav_setting_to_changePasswordFragment)
        }

        binding.paymentDetailsTV.setOnClickListener{

            Navigation.findNavController(view)
                .navigate(R.id.action_nav_setting_to_paymentDetailsFragment)
        }

        binding.subscriptionsTV.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_nav_setting_to_subscriptionsFragment)
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
                activity?.getSharedPreferences(Constants.LOGINPREF,Context.MODE_PRIVATE)?.edit {
                    clear()
                    apply()
                }
                LoginManager.getInstance().logOut()
                Constants.getEncryptedSharedPreference(Constants.SUBSCRIPTIONS,view.context).edit().clear().apply()
                val intent = Intent(activity, SignInActivity::class.java)
                dialog.dismiss()
                startActivity(intent)
                activity?.finish()
            }
            dialog.show()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}