package com.cg.cropdeal.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.SettingsFragmentBinding
import com.cg.cropdeal.viewmodel.SettingsVM


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