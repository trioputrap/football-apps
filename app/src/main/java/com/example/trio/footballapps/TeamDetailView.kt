package com.example.trio.footballapps

import com.example.trio.footballapps.model.PlayerItem

interface TeamDetailView {
    fun showLoading()
    fun hideLoading()
    fun showPlayers(data: List<PlayerItem>)
}