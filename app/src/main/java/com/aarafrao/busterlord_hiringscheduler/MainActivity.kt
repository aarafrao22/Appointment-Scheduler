package com.aarafrao.busterlord_hiringscheduler

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aarafrao.busterlord_hiringscheduler.databinding.ActivityMainBinding
import java.text.ParseException

class MainActivity : AppCompatActivity(), View.OnClickListener, ClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TripAdapter
    private lateinit var mutableList: MutableList<TripModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.btnFloat.setOnClickListener(this)

        mutableList = ArrayList()

        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))


        binding.rv.layoutManager = LinearLayoutManager(this)
        adapter = TripAdapter(mutableList, this, applicationContext)
        binding.rv.adapter = adapter

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                val filteredData = adapter.filterData(s.toString())
                adapter.tripModelList = filteredData
                adapter.notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


//        val databaseHelper: DatabaseHelper = DatabaseHelper.getDB(applicationContext)
//        val notifications: java.util.ArrayList<com.itecknologi.iteckapp.Database.Notification> =
//            databaseHelper.notificationDAO().getAllNotifications()
//
//
//        list = java.util.ArrayList<NotificationModel>()
//
//        for (var i = 0 i < notifications . size; i++) {
//
//            val context: String = notifications[i].getContext()
//            val img: Int
//
//            when (context) {
//                "1" -> img = R.drawable.battery_disconnected
//                "2" -> img = R.drawable.battery_connected
//                "3" -> img = R.drawable.fence
//                "4" -> img = R.drawable.ig_on
//                "5" -> img = R.drawable.ig_of
//                else -> img = R.drawable.logopie
//            }
//
//            try {
//                if (notifications.size > 0) {
//                    if (!isDateOlderThan7Days(java.lang.Long.valueOf(notifications[i].getTime()))) {
//                        val a: Int = list.size - i
//
////                        if (a > 0) {
//                        list.add(
//                            a, NotificationModel(
//                                img,
//                                notifications[i].getTitle(),
//                                notifications[i].getMessage(),
//                                unixToDate(notifications[i].getTime())
//                            )
//                        )
//                        //                        }
//                    } else {
//                        databaseHelper.notificationDAO().deleteNotification(notifications[i])
//                        notifications.remove(notifications[i])
//                    }
//                }
//            } catch (e: ParseException) {
//                e.printStackTrace()
//            }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnFloat -> startActivity(
                Intent(
                    applicationContext,
                    AddAppointmentActivity::class.java
                )
            )
        }
    }

    override fun onBackPressed() {

        mutableList = ArrayList()

        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))
        mutableList.add(TripModel("Interview", "iTecknologi", "98 min", "12-12-1", "9:20"))


    }

    override fun onItemClicked(position: Int) {
        Toast.makeText(applicationContext, "NONE", Toast.LENGTH_SHORT).show()
    }
}