package com.example.trio.footballapps.menu.team

import com.example.trio.footballapps.model.LeaguesItem
import com.example.trio.footballapps.model.TeamsItem

interface TeamView {
    fun showLoading()
    fun hideLoading()
    fun showLeagues(data: List<LeaguesItem>)
    fun showTeams(data: List<TeamsItem>)
    fun showSearched(data: List<TeamsItem>)
    fun closeSearch()
}