package com.cg.cropdeal.viewmodel

import android.app.Application
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

import androidx.lifecycle.AndroidViewModel
import com.cg.cropdeal.databinding.ActivityChangePasswordBinding
import com.cg.cropdeal.model.UtilActivity
import com.cg.cropdeal.view.MainActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePasswordVM(application: Application): AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    val utilActivity= UtilActivity()

    fun changePassword(binding: ActivityChangePasswordBinding){
        if (validatePasswordDetails(binding)) {
            val user = Firebase.auth.currentUser
            val credential = EmailAuthProvider
                .getCredential(user?.email!!, binding.editTextCurrentPassword.editText?.text.toString().trim { it<=' ' })

            val newPassword = binding.editTextNewPassword.editText?.text.toString().trim{it<= ' '}

            user?.reauthenticate(credential)?.addOnSuccessListener {

                user!!.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            utilActivity.showSnackbar("User Password Updated",
                                binding.changePasswordBtn)

                        } else {
                           utilActivity.showSnackbar(
                               "Couldn't update password. Please try again",
                            binding.changePasswordBtn)
                        }

                        startActivity(context,Intent(context, MainActivity::class.java),null)
                    }
            }
                .addOnFailureListener {
                    utilActivity.showSnackbar("Incorrect Current Password",
                        binding.changePasswordBtn)
                }

        }
    }


    private fun validatePasswordDetails(binding: ActivityChangePasswordBinding):Boolean{

        if(TextUtils.isEmpty(binding.editTextNewPassword.editText?.text.toString().trim { it<= ' ' })) {
            utilActivity.showSnackbar("Please enter New Password",
                    binding.editTextCurrentPassword)
            return false
        }

        if(TextUtils.isEmpty(binding.editTextConfirmNewPassword.editText?.text.toString()
                .trim { it<= ' ' })) {
            utilActivity.showSnackbar("Please confirm New Password",
                binding.editTextConfirmNewPassword)
            return false
        }


        if(binding.editTextConfirmNewPassword.editText?.text.toString() !=
            binding.editTextNewPassword.editText?.text.toString()) {
            utilActivity.showSnackbar(
                "New Password is not matching with Confirm New Password",
                    binding.changePasswordBtn)
            return false
        }

        if(binding.editTextCurrentPassword.editText?.text.toString() ==
            binding.editTextNewPassword.editText?.text.toString()) {
            utilActivity.showSnackbar(
                "New Password shouldn't be same as Current Password",
                binding.changePasswordBtn
            )
            return false
        }



        else {
            return true

        }

    }
    }
