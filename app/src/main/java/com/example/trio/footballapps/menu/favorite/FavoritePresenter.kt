package com.example.trio.footballapps.menu.favorite

import android.content.Context
import com.example.trio.footballapps.database.database
import com.example.trio.footballapps.model.EventsItem
import com.example.trio.footballapps.model.TeamsItem
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class FavoritePresenter(
        private val view: FavoriteView,
        private val ctx: Context
) {
    val TAG = this::class.java.simpleName

    fun getMatches() {
        view.showLoading()
        ctx.database.use {
            val result = select(EventsItem.TABLE_NAME)
            val favorite = result.parseList(classParser<EventsItem>())
            view.hideLoading()
            view.showMatches(favorite)
        }
    }

    fun getTeams() {
        view.showLoading()
        ctx.database.use {
            val result = select(TeamsItem.TABLE_NAME)
            val favorite = result.parseList(classParser<TeamsItem>())
            view.hideLoading()
            view.showTeams(favorite)
        }
    }
}