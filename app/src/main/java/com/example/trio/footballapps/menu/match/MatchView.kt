package com.example.trio.footballapps.menu.match

import com.example.trio.footballapps.model.EventsItem
import com.example.trio.footballapps.model.LeaguesItem

interface MatchView {
    fun showLoading()
    fun hideLoading()
    fun showLeagues(data: List<LeaguesItem>)
    fun showMatches(data: List<EventsItem>)
    fun showSearched(data: List<EventsItem>)
    fun closeSearch()
}