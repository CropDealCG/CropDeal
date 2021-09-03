package com.cg.cropdeal.view

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.cg.cropdeal.databinding.FragmentDealerManagementBinding
import com.cg.cropdeal.model.Constants
import com.google.firebase.database.FirebaseDatabase
import org.apache.poi.hssf.usermodel.HSSFWorkbook

import org.apache.poi.hssf.usermodel.HSSFCellStyle

import org.apache.poi.hssf.util.HSSFColor

import android.util.Log

import android.app.Activity
import android.content.Intent

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.AdminAddUserDialogBinding
import com.cg.cropdeal.model.AdminDealerAdapter
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.AdminDealerVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.CellStyle

import org.apache.poi.ss.usermodel.Cell


class AdminDealerFragment : Fragment() {
    private lateinit var binding: FragmentDealerManagementBinding
    private lateinit var workbook : Workbook
    private lateinit var firebaseDatabase:FirebaseDatabase
    private lateinit var progressDialog : AlertDialog
    private lateinit var viewModel: AdminDealerVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentDealerManagementBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminDealerVM::class.java)

        progressDialog = UtilRepo().loadingDialog(view.context)
        progressDialog.show()

        binding.adminDealerRview.layoutManager = LinearLayoutManager(view.context)
        viewModel.getDealerIdData()?.observe(viewLifecycleOwner,{ idList->
            viewModel.getDealerData()?.observe(viewLifecycleOwner,{list->
                if(idList.isEmpty() || idList!=null){
                    if(list.isEmpty() || list!=null){
                        binding.adminDealerRview.adapter = AdminDealerAdapter(list,idList)
                        progressDialog.dismiss()
                    }
                }
            })
        })
            binding.exportBtn.setOnClickListener {


                workbook= HSSFWorkbook()
                var  sheet:Sheet? = null
                val EXCEL_SHEET_NAME = "Dealers"




                sheet = workbook.createSheet(EXCEL_SHEET_NAME)
                val row: Row = sheet.createRow(0)
                var cell: Cell? = null

                val cellStyle = workbook.createCellStyle()
                cellStyle.fillForegroundColor = HSSFColor.AQUA.index
                cellStyle.fillPattern = HSSFCellStyle.SOLID_FOREGROUND
                cellStyle.alignment = CellStyle.ALIGN_CENTER

                cell = row.createCell(0)
                cell.setCellValue("Name")
                cell.setCellStyle(cellStyle)

                cell = row.createCell(1)
                cell.setCellValue("Email")
                cell.setCellStyle(cellStyle)

                cell = row.createCell(2)
                cell.setCellValue("DOB")
                cell.setCellStyle(cellStyle)


                cell?.cellStyle = cellStyle

                fillDataIntoExcel(sheet)
                permissionRequest.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            }
    }

    private fun fillDataIntoExcel(sheet:Sheet) {
        viewModel.getDealerData()?.observe(viewLifecycleOwner,{list->
            if(list.isEmpty() || list!=null){

                for(i in 1..list.size){
                    val row = sheet.createRow(i)
                    Log.d("Dealer",list[i-1].name)
                    var cell = row.createCell(0)
                    cell?.setCellValue(list[i-1].name)
                    cell = row.createCell(1)
                    cell?.setCellValue(list[i-1].email)
                    cell = row.createCell(2)
                    cell?.setCellValue(list[i-1].date)
                }

            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.admin_add_user_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.admin_add_user->{
                addDealer()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addDealer() {
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

    val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            viewModel.storeExcelInStorage(
                requireContext(),"Dealers.xls",workbook,requireView())
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Snackbar.make(requireView(),"Please allow Storage Permission",Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", View.OnClickListener {

                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)

                    }).setAnimationMode(Snackbar.ANIMATION_MODE_FADE).setActionTextColor(Color.parseColor("#ffff2222"))
                    .show()
            }else{
                Snackbar.make(requireView(),"Please Enable Storage Permissions from Settings",Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", View.OnClickListener {

                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package",requireActivity().packageName,null)
                        intent.data = uri
                        startActivity(intent)

                    }).setAnimationMode(Snackbar.ANIMATION_MODE_FADE).setActionTextColor(Color.parseColor("#ffff2222"))
                    .show()
            }
        }
    }
}