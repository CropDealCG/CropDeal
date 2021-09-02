package com.cg.cropdeal.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cg.cropdeal.databinding.FragmentDealerManagementBinding
import com.cg.cropdeal.model.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.apache.poi.hssf.usermodel.HSSFWorkbook

import org.apache.poi.hssf.usermodel.HSSFCellStyle

import org.apache.poi.hssf.util.HSSFColor

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.os.Environment

import android.app.Activity

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import com.cg.cropdeal.viewmodel.AdminDealerVM
import org.apache.poi.hssf.record.formula.functions.Column
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.CellStyle

import org.apache.poi.ss.usermodel.Cell





class AdminDealerFragment : Fragment() {
    private lateinit var binding: FragmentDealerManagementBinding
    private lateinit var fDatabase:FirebaseDatabase
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
        val dealerList = viewModel.getDealerData()


        binding.exportBtn.setOnClickListener {
            fDatabase = FirebaseDatabase.getInstance()
            val users = fDatabase.getReference(Constants.USERS)
            //val res = JsonTask().execute("Url address here");
//        val SDK_INT = Build.VERSION.SDK_INT
//        if (SDK_INT > 8) {
//            val policy = ThreadPolicy.Builder()
//                .permitAll().build()
//            StrictMode.setThreadPolicy(policy)
//
//
//            val response: JSONObject = getJSONObjectFromURL(
//                "https://cropdeal-a5248-default-rtdb.firebaseio.com/users/.json"
//            )
//
//        }
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

// Create a new sheet in a Workbook and assign a name to it
                sheet = workbook.createSheet(EXCEL_SHEET_NAME)
                val row: Row = sheet.createRow(0)
                var cell: Cell? = null

// Cell style for a cell

// Cell style for a cell
                val cellStyle = workbook.createCellStyle()
                cellStyle.fillForegroundColor = HSSFColor.AQUA.index
                cellStyle.fillPattern = HSSFCellStyle.SOLID_FOREGROUND
                cellStyle.alignment = CellStyle.ALIGN_CENTER

// Creating a cell and assigning it to a row

// Creating a cell and assigning it to a row
                cell = row.createCell(0)

// Setting Value and Style to the cell

// Setting Value and Style to the cell
                cell.setCellValue("First Cell")
                cell.cellStyle = cellStyle
            }
        }
    }

    private fun getJSONObjectFromURL(urlString: String): JSONObject {
        var urlConnection: HttpURLConnection? = null

        val url = URL(urlString)

        urlConnection = url.openConnection() as HttpURLConnection

        urlConnection.setRequestMethod("GET")
        urlConnection.setReadTimeout(10000 /* milliseconds */)
        urlConnection.setConnectTimeout(15000 /* milliseconds */)

        urlConnection.setDoOutput(true)

        urlConnection.connect()

        val br = BufferedReader(InputStreamReader(url.openStream()))

        val buffer = CharArray(1024)

        val jsonString: String

        val sb = StringBuilder()
        var line: String
        while (br.readLine().also { line = it } != null) {
            sb.append(
                """
            $line
            
            """.trimIndent()
            )
        }
        br.close()

        jsonString = sb.toString()

        Log.d("JSON", jsonString)
        urlConnection.disconnect()

        return JSONObject(jsonString)
    }

}