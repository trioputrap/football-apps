package com.example.trio.footballapps.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.trio.footballapps.R
import com.example.trio.footballapps.model.EventsItem
import com.example.trio.footballapps.utils.invisible
import com.example.trio.footballapps.utils.toStringNull
import com.example.trio.footballapps.utils.visible

class MatchAdapter(private val ctx: Context?, private var matches: List<EventsItem>) :
        RecyclerView.Adapter<MatchAdapter.ViewHolder>() {
    var listener: (EventsItem) -> Unit = {}
    var alarmListener: (EventsItem) -> Unit = {}

    fun setData(data: List<EventsItem>) {
        matches = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_list_match, parent, false))
    }

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: MatchAdapter.ViewHolder, position: Int) {
        holder.bindItem(matches[position], listener)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val alarm: ImageView = view.findViewById(R.id.alarm)
        val date: TextView = view.findViewById(R.id.date)
        val time: TextView = view.findViewById(R.id.time)
        val home_score: TextView = view.findViewById(R.id.home_score)
        val away_score: TextView = view.findViewById(R.id.away_score)
        val home_name: TextView = view.findViewById(R.id.home_name)
        val away_name: TextView = view.findViewById(R.id.away_name)

        fun bindItem(match: EventsItem, listener: (EventsItem) -> Unit) {
            date.text = match.getDateStringFromat("E, d MMM yyyy")
            time.text = match.getDateStringFromat("HH:mm")
            home_score.text = match.intHomeScore?.toStringNull()
            away_score.text = match.intAwayScore?.toStringNull()
            home_name.text = match.strHomeTeam.toString()
            away_name.text = match.strAwayTeam.toString()

            if (!match.isPast()) alarm.visible() else alarm.invisible()

            itemView.setOnClickListener { listener(match) }
            alarm.setOnClickListener { alarmListener(match) }
        }
    }
}