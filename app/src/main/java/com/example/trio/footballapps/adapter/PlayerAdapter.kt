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
import com.example.trio.footballapps.model.PlayerItem

class PlayerAdapter(private val ctx: Context, private val teams: List<PlayerItem>) :
        RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    var listener: (PlayerItem) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_list_player, parent, false))
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: PlayerAdapter.ViewHolder, position: Int) {
        holder.bindItem(teams[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profile: ImageView = view.findViewById(R.id.profile)
        val name: TextView = view.findViewById(R.id.name)
        val position: TextView = view.findViewById(R.id.position)

        fun bindItem(player: PlayerItem) {
            name.text = player.strPlayer
            position.text = player.strPosition
            Glide.with(ctx).load(player.strCutout).into(profile)
            itemView.setOnClickListener { listener(player) }
        }
    }
}