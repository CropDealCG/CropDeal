package com.cg.cropdeal.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

import com.cg.cropdeal.databinding.FragmentSubscriptionsBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.viewmodel.EditProfileVM
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class SubscriptionsFragment : Fragment() {

    private lateinit var viewModel:EditProfileVM
    private lateinit var binding: FragmentSubscriptionsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubscriptionsBinding.inflate(
            inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val sharedPreferences = EncryptedSharedPreferences.create(
            "subscriptions",
            masterKeyAlias,
            view.context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        viewModel = ViewModelProvider(this).get(EditProfileVM::class.java)



fun ChipGroup.addChip(context: Context, label: String){
    Chip(context).apply {
        id = View.generateViewId()
        text = label
        isClickable = true
        isCheckable = true
        isCheckedIconVisible = true
        isFocusable = true
        addView(this)
    }
}
        val chipGrp = binding.chipGroup
        for(crop in Constants.cropListWithoutAll) {
            chipGrp.addChip(requireContext(), crop)
        }
        binding.saveSubscriptionBtn.setOnClickListener {

            val selectedTopic = chipGrp.findViewById<Chip>(chipGrp.checkedChipId).text.toString()
            sharedPreferences?.edit()
                ?.putString("topic",selectedTopic)
                ?.apply()
           Constants.showSnackbar("Details of $selectedTopic subscribed",binding.root)
        }


    }

}