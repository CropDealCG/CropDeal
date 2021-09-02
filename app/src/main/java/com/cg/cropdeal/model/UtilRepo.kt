package com.cg.cropdeal.model

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.cg.cropdeal.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class UtilRepo() {

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
        val dialog =MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_App_MaterialAlertDialog)
//        return dialog.create()
        val layoutInflater =LayoutInflater.from(context)
        val customView = layoutInflater.inflate(layout,null,false)
        dialog.setView(customView)
        val materialDialog = dialog.create()
        materialDialog.setCancelable(true)
        return materialDialog
    }
    fun loadingDialog(context: Context) : AlertDialog{
        val dialog = MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_App_MaterialAlertDialog)
        val layoutInflater = LayoutInflater.from(context)
        val customView = layoutInflater.inflate(R.layout.custom_progress_dialog,null,false)
        dialog.setView(customView)
        dialog.setCancelable(false)
        return dialog.create()
    }

}