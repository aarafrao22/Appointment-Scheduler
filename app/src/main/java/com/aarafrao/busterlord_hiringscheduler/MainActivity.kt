package com.aarafrao.busterlord_hiringscheduler

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aarafrao.busterlord_hiringscheduler.Database.DatabaseHelper
import com.aarafrao.busterlord_hiringscheduler.Database.Model
import com.aarafrao.busterlord_hiringscheduler.databinding.ActivityMainBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


@Suppress("DEPRECATION")

class MainActivity : AppCompatActivity(), ClickListener {
    private lateinit var models: ArrayList<Model>
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var calendar: Calendar
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var adapter: Adapter
    var mutableList: MutableList<AppointModel> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = viewBinding.root
        setContentView(view)

        viewBinding.btnFloat.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext, AddAppointmentActivity::class.java
                )
            )
        }
        mutableList = ArrayList()

        viewBinding.todayBtn.setOnClickListener {

            if (mutableList.isNotEmpty()) {
                val a = mutableList.filter {
                    it.date == LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                }

                if (a.isNotEmpty()) {
                    adapter.appointModelList = a
                    adapter.notifyDataSetChanged()
                    showToast("Today Data Showed")
                } else {
                    showToast("No Appointments today")
                }
            } else {
                showToast("Please Add Some Data")
            }
        }

        databaseHelper = DatabaseHelper.getDB(applicationContext)
        models = databaseHelper.notificationDAO().allAppointments as ArrayList<Model>

        for (i in 0 until models.size) {

            try {
                if (models.size > 0) {

                    mutableList.add(
                        AppointModel(
                            models[i].title,
                            models[i].location,
                            models[i].duration,
                            models[i].date,
                            models[i].time
                        )
                    )
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
        calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 9)
        calendar.set(Calendar.YEAR, 2012)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.YEAR, 1)


        mutableList.sortBy { appointModel -> appointModel.time }
        viewBinding.rv.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(mutableList, this, applicationContext)
        viewBinding.rv.adapter = adapter
        adapter.notifyDataSetChanged()
        val d = getDrawable(R.drawable.bg_white)
        viewBinding.simpleCalendarView.selectedDateVerticalBar = d

        viewBinding.searchBar.addTextChangedListener(object : TextWatcher {
            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(s: Editable?) {

                if (s.toString() != "") {

                    val filteredData = adapter.filterData(s.toString())
                    adapter.appointModelList = filteredData
                    adapter.notifyDataSetChanged()
                    viewBinding.simpleCalendarView.visibility = View.GONE
                } else {
                    adapter.appointModelList = mutableList
                    adapter.notifyDataSetChanged()
                    viewBinding.simpleCalendarView.visibility = View.VISIBLE

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        //clean conflicts

        viewBinding.btnResolve.setOnClickListener {
            var count = 0

            if (mutableList.size <= 1) {
                return@setOnClickListener
            } else {
                mutableList.sortBy { model -> model.time }
            }

            for (i in 0 until mutableList.size) {

                val currentAppointment = mutableList[i]


                for (j in 0 until mutableList.size) {
                    val nextAppointment = mutableList[j]

                    val curTimeAddedWithDuration = LocalTime.parse(
                        currentAppointment.time, DateTimeFormatter.ofPattern("HH:mm")
                    ).plusMinutes(currentAppointment.duration.toLong())
                        .format(DateTimeFormatter.ofPattern("HH:mm"))

                    val nextTimeAddedWithDuration = LocalTime.parse(
                        nextAppointment.time, DateTimeFormatter.ofPattern("HH:mm")
                    ).plusMinutes(currentAppointment.duration.toLong())
                        .format(DateTimeFormatter.ofPattern("HH:mm"))



                    if (i != j) {
                        if (currentAppointment.date == nextAppointment.date) {

                            val greater =
                                getGreaterTime(curTimeAddedWithDuration, nextAppointment.time)



                            if (currentAppointment.time == nextAppointment.time) {

                                if (!currentAppointment.title.contains("(Postponed)")) {

                                    currentAppointment.title += " (Postponed)"
                                    count++
                                    currentAppointment.time = nextTimeAddedWithDuration
                                }

                            } else {

                                //check if current time + current duration
                                //is conflicting with next app time

                                if (isConflict(currentAppointment.time,currentAppointment.duration,nextAppointment.time)){


                                    if (!currentAppointment.title.contains("(Postponed)")) {

                                        currentAppointment.title += " (Postponed)"
                                        count++
                                        currentAppointment.time = nextTimeAddedWithDuration
                                    }
                                }
                            }

                        }
                    }
                }
            }

            viewBinding.rv.visibility = View.INVISIBLE

            Handler().postDelayed({
                mutableList.sortBy { appointModel -> appointModel.time }

                adapter.appointModelList = mutableList

                adapter.notifyDataSetChanged()
                viewBinding.rv.visibility = View.VISIBLE
                Toast.makeText(
                    applicationContext, "$count Conflicts Resolved", Toast.LENGTH_SHORT
                ).show()

            }, 600)

        }


        viewBinding.simpleCalendarView.setOnDateChangeListener { _, year, month, date ->

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val calendar = Calendar.getInstance()
            calendar[year, month] = date
            val formattedDate = dateFormat.format(calendar.time)


            val a: MutableList<AppointModel> = ArrayList()

            for (j in 0 until mutableList.size) {
                Log.d(TAG, "mutableList: " + mutableList[j].date)
                if (mutableList[j].date == formattedDate) {
                    a.add(mutableList[j])

                }
            }


            adapter.appointModelList = a
            adapter.notifyDataSetChanged()


        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isConflict(time: String, duration: String, time1: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val start = LocalDateTime.parse(time, formatter)
        val end = start.plus(Duration.parse(duration))
        val start1 = LocalDateTime.parse(time1, formatter)
        val end1 = start1.plus(Duration.parse(duration))

        return (start.isBefore(end1) && end.isAfter(start1)) || (start1.isBefore(end) && end1.isAfter(start))
    }



    private fun getGreaterTime(time1: String, time2: String): String {
        // Extract hours and minutes from time1
        val time1Hours = time1.substringBefore(":").toInt()
        val time1Minutes = time1.substringAfter(":").toInt()
        // Extract hours and minutes from time2
        val time2Hours = time2.substringBefore(":").toInt()
        val time2Minutes = time2.substringAfter(":").toInt()
        // Compare times and return greater time
        return if (time1Hours > time2Hours || (time1Hours == time2Hours && time1Minutes > time2Minutes)) {
            time1
        } else {
            time2
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }


    override fun onItemClicked(position: Int) {
        val shareText =
            mutableList[position].title + "\n At: " + mutableList[position].location + "\n Duration: " + mutableList[position].duration + " min \n Date: " + mutableList[position].date + "\n Time: " + mutableList[position].time

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)

        startActivity(Intent.createChooser(shareIntent, "Share using"))
    }

    override fun onDeleteClicked(position: Int) {
        showAlertDialogue2(R.drawable.baseline_more_horiz_24, position)

    }

    private fun showAlertDialogue2(icon: Int, position: Int) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle("Alert")
        builder1.setMessage("Do you want to Delete this Appointment")
        builder1.setIcon(icon)
        builder1.setCancelable(true)
        builder1.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder1.setPositiveButton(
            "OK"
        ) { dialog: DialogInterface, _: Int ->
            dialog.cancel()
            deleteItem(position)
        }
        val alert11 = builder1.create()
        if (!this@MainActivity.isFinishing) {
            alert11.show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteItem(position: Int) {
        val model = models[position]
        databaseHelper.notificationDAO().deleteNotification(model)
        Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_SHORT).show()
        databaseHelper = DatabaseHelper.getDB(applicationContext)
        models = databaseHelper.notificationDAO().allAppointments as ArrayList<Model>

        mutableList = ArrayList()
        for (i in 0 until models.size) {

            try {
                if (models.size > 0) {

                    mutableList.add(
                        AppointModel(
                            models[i].title,
                            models[i].location,
                            models[i].duration,
                            models[i].date,
                            models[i].time
                        )
                    )
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
        adapter.appointModelList = mutableList
        adapter.notifyDataSetChanged()

    }
}