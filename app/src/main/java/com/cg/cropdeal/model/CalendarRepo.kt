package com.cg.cropdeal.model

import android.app.Application
import android.content.Context
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class CalendarRepo(private var application: Application?) {


    fun selectDate(context: Context) : MaterialDatePicker<Long>{
        return MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()

    }
    fun selectTime(context: Context) : MaterialTimePicker{
        return MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(12).setMinute(0)
            .setTitleText("Select Time").build()
    }
}