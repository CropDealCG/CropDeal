package com.cg.cropdeal.view

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cg.cropdeal.databinding.FragmentDealerManagementBinding
import com.cg.cropdeal.model.Constants
import com.google.firebase.database.FirebaseDatabase
import org.apache.poi.hssf.usermodel.HSSFWorkbook

import org.apache.poi.hssf.usermodel.HSSFCellStyle

import org.apache.poi.hssf.util.HSSFColor

import android.util.Log

import android.app.Activity
import android.content.Context

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.model.AdminFarmerAdapter
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.AdminDealerVM
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.CellStyle

import org.apache.poi.ss.usermodel.Cell
import java.io.*


class AdminDealerFragment : Fragment() {
    private lateinit var binding: FragmentDealerManagementBinding
    private lateinit var firebaseDatabase:FirebaseDatabase
    private lateinit var progressDialog : AlertDialog
    private lateinit var viewModel: AdminDealerVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                        binding.adminDealerRview.adapter = AdminFarmerAdapter(list,idList)
                        progressDialog.dismiss()
                    }
                }
            })
        })





            binding.exportBtn.setOnClickListener {

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        (context as Activity?)!!,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )
                }

                val workbook: Workbook = HSSFWorkbook()
                var  sheet:Sheet? = null
                val EXCEL_SHEET_NAME = "Dealers"




                sheet = workbook.createSheet(EXCEL_SHEET_NAME)
                val row: Row = sheet.createRow(0)
                var cell: Cell? = null

                val cellStyle = workbook.createCellStyle()
                cellStyle.fillForegroundColor = HSSFColor.AQUA.index
                cellStyle.fillPattern = HSSFCellStyle.SOLID_FOREGROUND
                cellStyle.alignment = CellStyle.ALIGN_CENTER

                cell = row.createCell(0);
                cell.setCellValue("Name");
                cell.setCellStyle(cellStyle);

                cell = row.createCell(1);
                cell.setCellValue("Email");
                cell.setCellStyle(cellStyle);

                cell = row.createCell(2);
                cell.setCellValue("DOB");
                cell.setCellStyle(cellStyle);


                cell?.cellStyle = cellStyle

                fillDataIntoExcel(sheet)
                viewModel.storeExcelInStorage(
                    requireContext(),"Dealers.xls",workbook,view)
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




}





