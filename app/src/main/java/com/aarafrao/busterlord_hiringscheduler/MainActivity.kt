package com.aarafrao.busterlord_hiringscheduler

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aarafrao.busterlord_hiringscheduler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener, ClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TripAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.btnFloat.setOnClickListener(this)
        val mutableList: MutableList<TripModel> = ArrayList()
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

    override fun onItemClicked(position: Int) {
        Toast.makeText(applicationContext, "NONE", Toast.LENGTH_SHORT).show()
    }
}