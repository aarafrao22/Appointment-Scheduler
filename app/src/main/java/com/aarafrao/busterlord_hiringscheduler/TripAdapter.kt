package com.aarafrao.busterlord_hiringscheduler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripAdapter(tripModelList: List<TripModel>, clickListener: ClickListener, context: Context) :
    RecyclerView.Adapter<TripAdapter.VH>() {
    private val tripModelList: List<TripModel>
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
            tripModelList[position].date
        )
    }

    override fun getItemCount(): Int {
        return tripModelList.size
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val time1: TextView
        private val location1: TextView
        private val distance: TextView
        private val date: TextView

        init {
            time1 = itemView.findViewById(R.id.time1)
            location1 = itemView.findViewById(R.id.location1)
            distance = itemView.findViewById(R.id.duration)
            date = itemView.findViewById(R.id.date)
        }

        fun setData(
            time1: String,
            location2: String,
            distance: String,
            date: String
        ) {
            this.time1.text = time1
            this.location1.text = location2
            this.distance.text = distance
            this.date.text = date
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener.onItemClicked(adapterPosition)
        }
    }
}