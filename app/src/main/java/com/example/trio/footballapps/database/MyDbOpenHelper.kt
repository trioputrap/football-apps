package com.example.trio.footballapps.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.trio.footballapps.model.EventsItem
import com.example.trio.footballapps.model.TeamsItem
import org.jetbrains.anko.db.*

class MyDbOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FavoriteMatch.db", null, 7) {
    companion object {
        private var instance: MyDbOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDbOpenHelper {
            if (instance == null) {
                instance = MyDbOpenHelper(ctx.applicationContext)
            }
            return instance as MyDbOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
                EventsItem.TABLE_NAME, true,
                EventsItem.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                EventsItem.ID_EVENT to TEXT,
                EventsItem.EVENT_NAME to TEXT,
                EventsItem.EVENT_SPORT to TEXT,
                EventsItem.DATE_EVENT to TEXT,
                EventsItem.TIME_EVENT to TEXT,
                EventsItem.ID_HOME_TEAM to TEXT,
                EventsItem.ID_AWAY_TEAM to TEXT,
                EventsItem.HOME_TEAM_NAME to TEXT,
                EventsItem.AWAY_TEAM_NAME to TEXT,
                EventsItem.HOME_SCORE to TEXT,
                EventsItem.AWAY_SCORE to TEXT,
                EventsItem.HOME_SHOTS to TEXT,
                EventsItem.AWAY_SHOTS to TEXT,
                EventsItem.HOME_GOAL_DETAILS to TEXT,
                EventsItem.AWAY_GOAL_DETAILS to TEXT,
                EventsItem.HOME_GOAL_KEEPER to TEXT,
                EventsItem.AWAY_GOAL_KEEPER to TEXT,
                EventsItem.HOME_DEFENSE to TEXT,
                EventsItem.AWAY_DEFENSE to TEXT,
                EventsItem.HOME_MID_FIELD to TEXT,
                EventsItem.AWAY_MID_FIELD to TEXT,
                EventsItem.HOME_FORWARD to TEXT,
                EventsItem.AWAY_FORWARD to TEXT,
                EventsItem.HOME_SUBSTITUTES to TEXT,
                EventsItem.AWAY_SUBSTITUTES to TEXT
        )
        Log.e("TABLE", "TABLE MATCH CREATED")
        db.createTable(
                TeamsItem.TABLE_NAME, true,
                EventsItem.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                TeamsItem.ID_TEAM to TEXT,
                TeamsItem.TEAM_NAME to TEXT,
                TeamsItem.FORMED_YEAR to TEXT,
                TeamsItem.DESCRIPTION to TEXT,
                TeamsItem.STADIUM to TEXT,
                TeamsItem.BADGE to TEXT
        )
        Log.e("TABLE", "TABLE TEAM CREATED")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(EventsItem.TABLE_NAME, true)
    }
}

val Context.database: MyDbOpenHelper
    get() = MyDbOpenHelper.getInstance(applicationContext)