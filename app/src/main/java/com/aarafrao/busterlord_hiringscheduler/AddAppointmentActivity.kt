package com.aarafrao.busterlord_hiringscheduler

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aarafrao.busterlord_hiringscheduler.databinding.ActivityAddAppointmentBinding
import java.text.SimpleDateFormat
import java.util.*


class AddAppointmentActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddAppointmentBinding
    lateinit var myCalendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.apply {
            imgDatePicker.setOnClickListener {
                showDateTimePicker()
            }

            imgTimePicker.setOnClickListener {
                showTimePicker()
            }

            btnSave.setOnClickListener {

                validateEv()
            }

            imgClose.setOnClickListener {
                closeSc()
            }
        }


    }

    fun showDateTimePicker() {
        val currentDate = Calendar.getInstance()
        myCalendar = Calendar.getInstance()

        DatePickerDialog(
            this@AddAppointmentActivity, { view, year, monthOfYear, dayOfMonth ->

                myCalendar.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                binding.edDate.setText(dateFormat.format(myCalendar.time))
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE]
        ).show()


    }

    private fun validateEv() {
        if (binding.edName.text.toString() != "") {
            if (binding.edDate.text.toString() != "") {
                if (binding.edDuration.text.toString() != "") {
                    if (binding.edPlace.text.toString() != "") {
                        saveData()
                    } else binding.edPlaceLayout.error = "Invalid Input"
                } else binding.edDurationLayout.error = "Invalid Input"
            } else binding.edDateLayout.error = "Invalid Input"

        } else binding.edNameLayout.error = "Invalid Input"
    }

    private fun closeSc() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun saveData() {
        Toast.makeText(applicationContext, "Data Saved", Toast.LENGTH_SHORT).show()
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.edDate.setText(dateFormat.format(myCalendar.time))
    }

    private fun showTimePicker() {
        val currentDate = Calendar.getInstance()
        myCalendar = Calendar.getInstance()

        TimePickerDialog(
            this@AddAppointmentActivity, { view, hourOfDay, minute ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, minute)
                Log.v(TAG, "The choosen one " + myCalendar.getTime())

                val dateFormat = SimpleDateFormat("hh:mm aa", Locale.US)
                binding.edTime.setText(dateFormat.format(myCalendar.time))

            }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false
        ).show()
    }
}