package com.cg.cropdeal.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.UserHandle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.LogoutDialogBinding
import com.cg.cropdeal.databinding.SettingsFragmentBinding
import com.cg.cropdeal.model.Users
import com.cg.cropdeal.viewmodel.SettingsVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

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
        binding.editProfTV.setOnClickListener {
//            viewModel.activityToStart.observe(viewLifecycleOwner, Observer { value ->
                val intent = Intent(activity, EditProfileActivity::class.java)

                startActivity(intent)
           // })
        }

        binding.changePassTV.setOnClickListener {
            val intent = Intent(activity,ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.sendFeedbackTV.setOnClickListener{
            val email = Intent(Intent.ACTION_SEND)

            val mail={"cropdeals@gmail.com"}.toString()
            email.putExtra(Intent.EXTRA_EMAIL, mail)
            email.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            email.type = "message/rfc822"

            startActivity(
                Intent.createChooser(email, "Choose an Email client :"), null
            )
        }

        binding.aboutUsTV.setOnClickListener {
            val intent = Intent(activity,AboutUsActivity::class.java)
            startActivity(intent)
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
        viewModel = ViewModelProvider(this).get(SettingsVM::class.java)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}