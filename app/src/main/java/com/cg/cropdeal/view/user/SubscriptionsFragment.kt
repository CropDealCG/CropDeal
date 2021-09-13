package com.cg.cropdeal.view.user

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cg.cropdeal.R

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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSubscriptionsBinding.inflate(
            inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences=Constants.getEncryptedSharedPreference(Constants.SUBSCRIPTIONS,view.context)

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
        Constants.cropsList.observe(viewLifecycleOwner,{
            for(crop in it) {
                if(crop!="All")
                    chipGrp.addChip(requireContext(), crop)
            }
        })

        val selectedSet = mutableSetOf<String>()

        binding.saveSubscriptionBtn.setOnClickListener {
            selectedSet.clear()
            val list = chipGrp.checkedChipIds
            for(crop in list){
                selectedSet.add(chipGrp.findViewById<Chip>(crop).text.toString())
            }
            sharedPreferences.edit().clear().apply()
            sharedPreferences.edit().putStringSet("topic",selectedSet).apply()
            Constants.showSnackbar(getString(R.string.cropSubbed),binding.root)
            Navigation.findNavController(view).popBackStack()
        }


    }

}