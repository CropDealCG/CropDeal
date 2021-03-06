package com.cg.cropdeal.model.repo

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.cg.cropdeal.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UtilRepo {

    fun selectDate() : MaterialDatePicker<Long>{
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