package com.example.trio.footballapps

import com.example.trio.footballapps.model.TeamsItem

interface MatchDetailView {
    fun showHomeBadgeLoading()
    fun hideHomeBadgeLoading()
    fun showAwayBadgeLoading()
    fun hideAwayBadgeLoading()
    fun showHomeTeam(data: TeamsItem)
    fun showAwayTeam(data: TeamsItem)
}