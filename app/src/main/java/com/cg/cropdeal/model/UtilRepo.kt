package com.cg.cropdeal.model

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class UtilRepo(private var application: Application?) {

    fun selectDate(context: Context) : MaterialDatePicker<Long>{
        return MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()

    }
    fun selectTime(context: Context) : MaterialTimePicker{
        return MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(12).setMinute(0)
            .setTitleText("Select Time").build()
    }
    fun customDialog(context: Context,layout: Int) : AlertDialog {
        val dialog =MaterialAlertDialogBuilder(context)
//        return dialog.create()
        val layoutInflater =LayoutInflater.from(context)
        val customView = layoutInflater.inflate(layout,null,false)
        dialog.setView(customView)
        val materialDialog = dialog.create()
        materialDialog.setCancelable(false)
        return materialDialog
    }
}