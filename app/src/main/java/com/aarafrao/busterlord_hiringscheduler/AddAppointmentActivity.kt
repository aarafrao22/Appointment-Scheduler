package com.aarafrao.busterlord_hiringscheduler

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aarafrao.busterlord_hiringscheduler.Database.DatabaseHelper
import com.aarafrao.busterlord_hiringscheduler.Database.Model
import com.aarafrao.busterlord_hiringscheduler.databinding.ActivityAddAppointmentBinding
import java.text.SimpleDateFormat
import java.util.*


class AddAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAppointmentBinding
    private lateinit var myCalendar: Calendar

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

    private fun showDateTimePicker() {
        val currentDate = Calendar.getInstance()
        myCalendar = Calendar.getInstance()

        DatePickerDialog(
            this@AddAppointmentActivity, { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                binding.edDate.setText(dateFormat.format(myCalendar.time))
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE]
        ).show()


    }

    private fun validateEv() {
        if (binding.edName.text.toString() != "") {
            if (binding.edDate.text.toString() != "") {
                if (binding.edDuration.text.toString() != "") {
                    if (binding.edPlace.text.toString() != "") {
                        if (binding.edPhone.text.toString() != "") {

                            if (!isConflict())
                                saveData()
                            else
                                Toast.makeText(
                                    this@AddAppointmentActivity,
                                    "found conflict",
                                    Toast.LENGTH_SHORT
                                ).show()

                        } else binding.edPhoneLayout.error = "Invalid Input"
                    } else binding.edPlaceLayout.error = "Invalid Input"
                } else binding.edDurationLayout.error = "Invalid Input"
            } else binding.edDateLayout.error = "Invalid Input"

        } else binding.edNameLayout.error = "Invalid Input"
    }

    private fun isConflict(): Boolean {

        return false
    }

    private fun closeSc() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun saveData() {
        Toast.makeText(applicationContext, "Data Saved", Toast.LENGTH_SHORT).show()


//        val model =

//        val mainActivity = MainActivity()
//        mainActivity.addElement(model)


        val databaseHelper: DatabaseHelper = DatabaseHelper.getDB(applicationContext)
        databaseHelper.notificationDAO().addAppointment(
            Model(
                binding.edName.text.toString(),
                binding.edPlace.text.toString(),
                binding.edDuration.text.toString(),
                binding.edTime.text.toString(),
                binding.edDate.text.toString(),
                binding.edPhone.text.toString()
            )
        )

        val models: MutableList<Model>? =
            databaseHelper.notificationDAO().allAppointments
        for (i in models!!.indices) {
            Log.d(TAG, "saveData: $models.")
        }
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }


    private fun showTimePicker() {
        val currentDate = Calendar.getInstance()
        myCalendar = Calendar.getInstance()

        TimePickerDialog(
            this@AddAppointmentActivity, { _, hourOfDay, minute ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, minute)
                Log.v(TAG, "The chosen one " + myCalendar.time)

                val dateFormat = SimpleDateFormat("HH:mm", Locale.US)
                binding.edTime.setText(dateFormat.format(myCalendar.time))

            }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false
        ).show()
    }
}