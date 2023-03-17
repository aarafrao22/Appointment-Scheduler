package com.aarafrao.busterlord_hiringscheduler

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aarafrao.busterlord_hiringscheduler.Database.DatabaseHelper
import com.aarafrao.busterlord_hiringscheduler.Database.Notification
import com.aarafrao.busterlord_hiringscheduler.databinding.ActivityMainBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


@Suppress("DEPRECATION")

class MainActivity : AppCompatActivity(), ClickListener {

    private lateinit var calendar: Calendar
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var adapter: TripAdapter
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
            Toast.makeText(applicationContext, "Today Data Showed", Toast.LENGTH_SHORT).show()

            if (mutableList.size > 0) {

                val a: MutableList<AppointModel> = ArrayList()
                for (i in 0 until mutableList.size - 1) {

                    val currentAppointment = mutableList[i]
                    val currentDate: LocalDate = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val formattedDate: String = currentDate.format(formatter)


                    if (currentAppointment.date == formattedDate) {
                        //here
                        a.add(currentAppointment)

                    }
                }

                adapter.appointModelList = a
                adapter.notifyDataSetChanged()

            }
        }
        val databaseHelper: DatabaseHelper = DatabaseHelper.getDB(applicationContext)
        val notifications: ArrayList<Notification> =
            databaseHelper.notificationDAO().allNotifications as ArrayList<Notification>

        for (i in 0 until notifications.size) {

            try {
                if (notifications.size > 0) {

                    mutableList.add(
                        AppointModel(
                            notifications[i].title,
                            notifications[i].location,
                            notifications[i].duration,
                            notifications[i].date,
                            notifications[i].time
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


        viewBinding.rv.layoutManager = LinearLayoutManager(this)
        adapter = TripAdapter(mutableList, this, applicationContext)
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


        viewBinding.btnResolve.setOnClickListener {

            if (mutableList.size <= 1) {
                return@setOnClickListener
            }

            for (i in 0 until mutableList.size) {

                val currentAppointment = mutableList[i]


                for (j in 0 until mutableList.size) {

                    if (i!=j){
                        if (currentAppointment.date == mutableList[j].date &&
                            currentAppointment.time == mutableList[j].time
                        ) {

                            mutableList[j].title += " (Postponed)"

                            val time = LocalTime.parse(mutableList[j].time,
                                DateTimeFormatter.ofPattern("HH:mm")
                            ).plusMinutes(currentAppointment.duration.toLong())

                            mutableList[j].time = time.format(DateTimeFormatter.ofPattern("HH:mm"))
                        }
                    }
                }
//                val nextAppointment = mutableList[i + 1]

//                if (currentAppointment.date == nextAppointment.date &&
//                    currentAppointment.time == nextAppointment.time
//                ) {
//                    nextAppointment.title += " (Postponed)"
//
//                    val time = LocalTime.parse(
//                        nextAppointment.time,
//                        DateTimeFormatter.ofPattern("HH:mm")
//                    ).plusMinutes(currentAppointment.duration.toLong())
//                    nextAppointment.time = time.format(DateTimeFormatter.ofPattern("HH:mm"))
//                }
            }

            viewBinding.rv.visibility = View.INVISIBLE
            Handler().postDelayed({

                adapter.appointModelList = mutableList
                adapter.notifyDataSetChanged()
                viewBinding.rv.visibility = View.VISIBLE
                Toast.makeText(
                    applicationContext,
                    "Conflicts Resolved",
                    Toast.LENGTH_SHORT
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


    override fun onItemClicked(position: Int) {
        AppointModel(
            mutableList[position].title,
            mutableList[position].location,
            mutableList[position].duration,
            mutableList[position].date,
            mutableList[position].time
        )
    }
}