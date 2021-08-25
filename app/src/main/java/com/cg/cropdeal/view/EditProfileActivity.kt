package com.cg.cropdeal.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.ActivityEditProfileBinding
import com.cg.cropdeal.databinding.SettingsFragmentBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.UtilActivity
import com.cg.cropdeal.viewmodel.EditProfileVM
import com.cg.cropdeal.viewmodel.SettingsVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import com.cg.cropdeal.model.FirebaseQueryLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.text.SimpleDateFormat
import java.util.*


class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewModel: EditProfileVM
    private var _binding : ActivityEditProfileBinding? = null
    private val binding get() = _binding!!



    private var selectedImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditProfileVM::class.java)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.editEmailET.isEnabled = false
        val liveData = viewModel.getDataSnapshotLiveData()

        liveData.observe(this,
            { dataSnapshot ->
                if (dataSnapshot != null) {

                    val username =
                        dataSnapshot.child(Constants.USERNAME).value.toString()
                    binding.editUserNameET.editText?.setText(username)

                    val email = dataSnapshot.child(Constants.EMAIL).value.toString()
                    binding.editEmailET.editText?.setText(email)

                    val dob = dataSnapshot.child(Constants.DATE).value.toString()
                    binding.editDobTV.setText(dob)
                }
            })




        val profile_image_ref = getSharedPreferences(Constants.PROFILE_IMAGE_REF,0)
        val uri = profile_image_ref?.getString("profile_image","")


        Glide.with(this )
            .load(uri)
            .circleCrop()
            .placeholder(R.drawable.blank_profile)
            .into(binding.profileImage)

        binding.profileImage.setOnClickListener(this@EditProfileActivity)
        binding.saveProfileButton.setOnClickListener(this@EditProfileActivity)

        binding.editDobTV.setOnClickListener{
            val datePickerDialog = viewModel.selectDate(this)
            datePickerDialog.addOnPositiveButtonClickListener { dateInMillis->
                val date = SimpleDateFormat("MMM dd, yyyy",
                    Locale.getDefault()).format(Date(dateInMillis))
                binding.editDobTV.setText(date)
            }
            datePickerDialog.show(supportFragmentManager,"Date")
        }
    }






    override fun onClick(v: View?) {
        when(v?.id){
            R.id.profileImage ->{
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){

                        UtilActivity().showSnackbar(
                            "Storage permission already granted",
                            binding.editProfileLyt
                        )

                    Constants.showImageChooser(this)
                }else{
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.READ_STORAGE_PERMISSION_CODE
                    )
                }
            }

            R.id.saveProfileButton ->{

                    if(selectedImageFileUri != null) {
                        uploadImageToCloudStorage(this, selectedImageFileUri)
                    }
                    else{
                        updateUserProfileDetails()
                    }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                Constants.showImageChooser(this)
            }else{
            UtilActivity().showSnackbar("Storage permission Denied",binding.editProfileLyt)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode== Constants.PICK_IMAGE_REQUEST_CODE){
                if(data!=null){
                    try{
                        selectedImageFileUri = data.data!!
                        // profileImg.setImageURI((selectedImageFileUri))
                        loadUserPicture(selectedImageFileUri!!,binding.profileImage)
                    } catch (e: IOException){
                        e.printStackTrace()
                        UtilActivity().showSnackbar("Image selection failed",
                            binding.editProfileLyt)
                    }
                }
            }
        }
    }


    fun loadUserPicture(imageUri: Uri, imageView: ImageView){
        try{
            Glide.with(applicationContext)
                .load(imageUri)
                .centerCrop()
                .into(imageView)
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun updateUserProfileDetails(){
        val username = binding.editUserNameET.editText?.text.toString().trim{it<=' '}
        val dob = binding.editDobTV.text.toString().trim{it<=' '}
        viewModel.updateUserProfileDetails(username,dob)


        UtilActivity().showSnackbar("Profile updated successfully",
            binding.editProfileLyt)

        startActivity(Intent(this@EditProfileActivity, MainActivity::class.java))
        finish()
    }





    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?) {
        val username = binding.editUserNameET.editText?.text.toString().trim{it<=' '}
        val dob = binding.editDobTV.text.toString().trim{it<=' '}
        viewModel.uploadImageToCloudStorage(activity,imageFileUri,username,dob)
    }
}