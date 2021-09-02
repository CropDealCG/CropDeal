package com.cg.cropdeal.view

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
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.CellStyle

import org.apache.poi.hssf.usermodel.HSSFCellStyle

import org.apache.poi.hssf.util.HSSFColor

import org.apache.poi.ss.usermodel.Cell
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL


class AdminDealerFragment : Fragment() {
    private lateinit var binding: FragmentDealerManagementBinding
    private lateinit var fDatabase:FirebaseDatabase
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
        fDatabase = FirebaseDatabase.getInstance()
        val users = fDatabase.getReference(Constants.USERS)
        //val res = JsonTask().execute("Url address here");
        val response: JSONObject = getJSONObjectFromURL(
            "https://cropdeal-a5248-default-rtdb.firebaseio.com/users/.json")


        binding.exportBtn.setOnClickListener{
            val workbook= HSSFWorkbook()
            var sheet: Sheet? = null;
           val EXCEL_SHEET_NAME = "Sheet1"

// Create a new sheet in a Workbook and assign a name to it
            sheet = workbook.createSheet(EXCEL_SHEET_NAME);
            val row = sheet.createRow(0)
            var cell: Cell? = null


            val cellStyle: CellStyle = workbook.createCellStyle()
            cellStyle.fillForegroundColor = HSSFColor.AQUA.index
            cellStyle.fillPattern = HSSFCellStyle.SOLID_FOREGROUND
            cellStyle.alignment = CellStyle.ALIGN_CENTER

// Creating a cell and assigning it to a row

// Creating a cell and assigning it to a row
            cell = row.createCell(0)

// Setting Value and Style to the cell

// Setting Value and Style to the cell
            cell.setCellValue("First Cell")
            cell.setCellStyle(cellStyle)
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

        var jsonString = String()

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

        println("JSON: $jsonString")
        urlConnection.disconnect()

        return JSONObject(jsonString)
    }
}