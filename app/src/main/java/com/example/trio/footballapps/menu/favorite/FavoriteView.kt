package com.example.trio.footballapps.menu.favorite

import com.example.trio.footballapps.model.EventsItem
import com.example.trio.footballapps.model.TeamsItem

interface FavoriteView {
    fun showLoading()
    fun hideLoading()
    fun showMatches(data: List<EventsItem>)
    fun showTeams(data: List<TeamsItem>)
}