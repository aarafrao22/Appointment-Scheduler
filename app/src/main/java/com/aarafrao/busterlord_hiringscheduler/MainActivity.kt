package com.aarafrao.busterlord_hiringscheduler

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aarafrao.busterlord_hiringscheduler.Database.DatabaseHelper
import com.aarafrao.busterlord_hiringscheduler.Database.Notification
import com.aarafrao.busterlord_hiringscheduler.databinding.ActivityMainBinding
import java.text.ParseException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnClickListener, ClickListener {
    private lateinit var calendar: Calendar
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TripAdapter
    var mutableList: MutableList<AppointModel> = ArrayList()


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.btnFloat.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext, AddAppointmentActivity::class.java
                )
            )
        }
        mutableList = ArrayList()

        binding.todayBtn.setOnClickListener {
            Toast.makeText(applicationContext, "Today Data Showed", Toast.LENGTH_SHORT).show()
        }
        val databaseHelper: DatabaseHelper = DatabaseHelper.getDB(applicationContext)
        val notifications: java.util.ArrayList<Notification> =
            databaseHelper.notificationDAO().allNotifications as java.util.ArrayList<Notification>

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


        binding.rv.layoutManager = LinearLayoutManager(this)
        adapter = TripAdapter(mutableList, this, applicationContext)
        binding.rv.adapter = adapter
        adapter.notifyDataSetChanged()
        val d = getDrawable(R.drawable.bg_white)
        binding.simpleCalendarView.selectedDateVerticalBar = d

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(s: Editable?) {

                if (s.toString() != "") {

                    val filteredData = adapter.filterData(s.toString())
                    adapter.appointModelList = filteredData
                    adapter.notifyDataSetChanged()
                    binding.simpleCalendarView.visibility = View.GONE
                } else {
                    adapter.appointModelList = mutableList
                    adapter.notifyDataSetChanged()
                    binding.simpleCalendarView.visibility = View.VISIBLE

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.simpleCalendarView.setOnDateChangeListener { calendarView, i, i1, i2 ->
            val msg = i2.toString() + "/" + i1.plus(1) + " Year " + i
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
            val a: MutableList<AppointModel> = ArrayList()

            for (j in 0 until mutableList.size) {

                if (mutableList[j].date == i2.toString()) {
                    a.add(mutableList[j])
                }
            }


        }


    }


    override fun onClick(p0: View?) {
        adapter.notifyDataSetChanged()
    }

    public fun addElement(model: AppointModel) {
        if (mutableList != null) {
            mutableList.add(model)
        } else {
            mutableList = ArrayList()
            mutableList.add(model)

        }
    }

    override fun onItemClicked(position: Int) {
        val model = AppointModel(
            mutableList[position].title,
            mutableList[position].location,
            mutableList[position].duration,
            mutableList[position].date,
            mutableList[position].time
        )
    }
}