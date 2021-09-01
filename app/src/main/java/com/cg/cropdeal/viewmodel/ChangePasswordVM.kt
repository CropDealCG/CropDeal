package com.cg.cropdeal.viewmodel

import android.app.Application
import android.text.TextUtils
import android.widget.Toast

import androidx.lifecycle.AndroidViewModel
import com.cg.cropdeal.databinding.FragmentChangePasswordBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.UtilRepo
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePasswordVM(application: Application): AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private var utilRepo:UtilRepo?=null
    init {
        utilRepo = UtilRepo(application)
    }

    fun changePassword(binding: FragmentChangePasswordBinding):Boolean{

        var result = false
            val user = Firebase.auth.currentUser
            val credential = EmailAuthProvider
                .getCredential(user?.email!!, binding.editTextCurrentPassword.editText?.text.toString().trim { it<=' ' })

            val newPassword = binding.editTextNewPassword.editText?.text.toString().trim{it<= ' '}

            user.reauthenticate(credential).addOnSuccessListener {

                user.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context,"User Password Updated",Toast.LENGTH_LONG)
                                .show()

                        } else {
                            Toast.makeText(context,
                                "Couldn't update password. Please try again",Toast.LENGTH_LONG)
                                .show()

                        }
                        result = true

                    }
            }
                .addOnFailureListener {
                   Toast.makeText(context,it.message,Toast.LENGTH_LONG)
                }

                return result


    }


    fun validatePasswordDetails(binding: FragmentChangePasswordBinding):Boolean{

        if(TextUtils.isEmpty(binding.editTextNewPassword.editText?.text.toString().trim { it<= ' ' })) {
            Constants.showSnackbar("Please enter New Password"
                ,binding.root)
            return false
        }
        if(binding.editTextNewPassword.editText?.text?.length!! < 9)
        {
            Constants.showSnackbar("Password length should be min 9 char"
                ,binding.root)
            return false
        }

        if(TextUtils.isEmpty(binding.editTextConfirmNewPassword.editText?.text.toString()
                .trim { it<= ' ' })) {
            Constants.showSnackbar("Please confirm New Password",
                binding.root)
            return false
        }


        if(binding.editTextConfirmNewPassword.editText?.text.toString() !=
            binding.editTextNewPassword.editText?.text.toString()) {
            Constants.showSnackbar(
                "New Password is not matching with Confirm New Password"
                ,binding.root)
            return false
        }

        if(binding.editTextCurrentPassword.editText?.text.toString() ==
            binding.editTextNewPassword.editText?.text.toString()) {
            Constants.showSnackbar(
                "New Password shouldn't be same as Current Password"
                ,binding.root
            )
            return false
        }



        else {
            return true

        }

    }
}
