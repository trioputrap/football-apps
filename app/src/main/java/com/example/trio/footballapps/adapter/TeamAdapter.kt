package com.example.trio.footballapps.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.trio.footballapps.R
import com.example.trio.footballapps.model.TeamsItem

class TeamAdapter(private val ctx: Context, private var teams: List<TeamsItem>) :
        RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    var listener: (TeamsItem) -> Unit = {}

    fun setData(data: List<TeamsItem>) {
        teams = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_list_team, parent, false))
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: TeamAdapter.ViewHolder, position: Int) {
        holder.bindItem(teams[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val badge: ImageView = view.findViewById(R.id.badge)
        val name: TextView = view.findViewById(R.id.name)

        fun bindItem(team: TeamsItem) {
            name.text = team.strTeam
            Glide.with(ctx).load(team.strTeamBadge).into(badge)
            itemView.setOnClickListener { listener(team) }
        }
    }
}