package com.cg.cropdeal.view
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentChangePasswordBinding
import com.cg.cropdeal.viewmodel.ChangePasswordVM
private lateinit var binding: FragmentChangePasswordBinding

class ChangePasswordFragment : Fragment() {
    private lateinit var viewModel : ChangePasswordVM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangePasswordVM::class.java)
        binding.changePasswordBtn.setOnClickListener {
            viewModel.changePassword(binding)
            Navigation.findNavController(view)
                .navigate(R.id.action_changePasswordFragment_to_nav_setting)
        }

    }
}