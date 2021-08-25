package com.cg.cropdeal.model

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class UtilRepo(private var application: Application?) {

    fun selectDate(context: Context) : MaterialDatePicker<Long>{
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
        return MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

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