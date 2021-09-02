package com.cg.cropdeal.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.AdminAddUserDialogBinding
import com.cg.cropdeal.databinding.FragmentFarmerManagementBinding
import com.cg.cropdeal.model.AdminFarmerAdapter
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.AdminFarmerVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AdminFarmerFragment : Fragment() {
    private lateinit var binding : FragmentFarmerManagementBinding
    private lateinit var viewModel : AdminFarmerVM
    private lateinit var progressDialog : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentFarmerManagementBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminFarmerVM::class.java)

        progressDialog = UtilRepo().loadingDialog(view.context)
        progressDialog.show()

        binding.adminFarmerRview.layoutManager = LinearLayoutManager(view.context)
        viewModel.getFarmerIdList()?.observe(viewLifecycleOwner,{ idList->
            viewModel.getFarmerList()?.observe(viewLifecycleOwner,{list->
                if(idList.isEmpty() || idList!=null){
                    if(list.isEmpty() || list!=null){
                        binding.adminFarmerRview.adapter = AdminFarmerAdapter(list,idList)
                        progressDialog.dismiss()
                    }
                }
            })
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.admin_add_user_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.admin_add_user->{
                addFarmer()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addFarmer() {
        val dialog = MaterialAlertDialogBuilder(view?.context!!,R.style.ThemeOverlay_App_MaterialAlertDialog)
        val customBinding = AdminAddUserDialogBinding.inflate(layoutInflater)
        dialog.setView(customBinding.root)
        dialog.setTitle("Are you sure?")
        dialog.setMessage("You will be logged out by this action")
        var dialogBuilder = dialog.create()
        customBinding.adminAddUserDialogBtn.setOnClickListener {
            val name = customBinding.adminAddUserDialogNameTV.editText?.text.toString()
            val email = customBinding.adminAddUserDialogEmailTV.editText?.text.toString()
            val password = customBinding.adminAddUserDialogPasswordTV.editText?.text.toString()
            if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
                Constants.showSnackbar("Please enter all the details",customBinding.root)
            }
            else{
                viewModel.registerUser(name,email,password,binding.root)
                dialogBuilder.dismiss()
                startActivity(Intent(customBinding.root.context, SignInActivity::class.java))
                activity?.finish()
            }
        }
        dialogBuilder = dialog.create()
        dialogBuilder.show()
    }

}