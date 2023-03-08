package com.aarafrao.busterlord_hiringscheduler

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aarafrao.busterlord_hiringscheduler.databinding.ActivityAddAppointmentBinding
import java.text.SimpleDateFormat
import java.util.*


class AddAppointmentActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddAppointmentBinding
    val myCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date =
            OnDateSetListener { view, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }


        binding.edTime.setOnClickListener {
            showTimePicker()
        }
        binding.edDate.setOnClickListener {
            DatePickerDialog(
                this@AddAppointmentActivity,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()

        }
        binding.btnSave.setOnClickListener {

            validateEv()
        }

        binding.imgClose.setOnClickListener {
            closeSc()
        }
    }

    private fun validateEv() {
        if (binding.edName.text != null) {
            if (binding.edDate.text != null) {
                if (binding.edDuration.text != null) {
                    if (binding.edPlace.text != null) {
                        saveData()
                    } else binding.edPlaceLayout.error = "Invalid Input"
                } else binding.edDateLayout.error = "Invalid Input"
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
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            this@AddAppointmentActivity,
            { timePicker, selectedHour, selectedMinute -> }, hour, minute, false
        )
        timePickerDialog.show()
    }
}