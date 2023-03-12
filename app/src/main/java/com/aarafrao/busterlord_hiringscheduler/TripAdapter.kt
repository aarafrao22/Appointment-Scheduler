package com.aarafrao.busterlord_hiringscheduler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripAdapter(tripModelList: List<TripModel>, clickListener: ClickListener, context: Context) :
    RecyclerView.Adapter<TripAdapter.VH>() {
    var tripModelList: List<TripModel>
    private val clickListener: ClickListener
    private val context: Context

    init {
        this.tripModelList = tripModelList
        this.clickListener = clickListener
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.setData(
            tripModelList[position].time,
            tripModelList[position].location,
            tripModelList[position].duration,
            tripModelList[position].date,
            tripModelList[position].title
        )
    }

    override fun getItemCount(): Int {
        return tripModelList.size
    }

    fun filterData(query: String): List<TripModel> {
        val filteredList = mutableListOf<TripModel>()
        for (data in tripModelList) {
            if (data.title.contains(query, ignoreCase = true) /*|| data.date.contains(query, ignoreCase = true) ||
                data.location.contains(query, ignoreCase = true) || data.duration.contains(query, ignoreCase = true)*/) {
                filteredList.add(data)
            }

        }
        return filteredList
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val time1: TextView
        private val location1: TextView
        private val distance: TextView
        private val date: TextView
        private val title:TextView

        init {
            time1 = itemView.findViewById(R.id.time1)
            location1 = itemView.findViewById(R.id.location1)
            distance = itemView.findViewById(R.id.duration)
            date = itemView.findViewById(R.id.date)
            title = itemView.findViewById(R.id.meetingTitle)
        }

        fun setData(
            time1: String,
            location2: String,
            distance: String,
            date: String,
            title:String
        ) {
            this.time1.text = time1
            this.location1.text = location2
            this.distance.text = distance
            this.date.text = date
            this.title.text = title

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener.onItemClicked(adapterPosition)
        }
    }
}