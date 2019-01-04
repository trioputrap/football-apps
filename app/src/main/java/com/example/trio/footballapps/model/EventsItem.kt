package com.example.trio.footballapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class EventsItem(
        var id: Int? = null,

        @field:SerializedName("idEvent")
        val idEvent: String? = null,

        @field:SerializedName("strEvent")
        val strEvent: String? = null,

        @field:SerializedName("strSport")
        val strSport: String? = null,

        @field:SerializedName("dateEvent")
        val dateEvent: String? = null,

        @field:SerializedName("strTime")
        val strTime: String? = null,

        @field:SerializedName("idHomeTeam")
        val idHomeTeam: String? = null,

        @field:SerializedName("idAwayTeam")
        val idAwayTeam: String? = null,

        @field:SerializedName("strHomeTeam")
        val strHomeTeam: String? = null,

        @field:SerializedName("strAwayTeam")
        val strAwayTeam: String? = null,

        @field:SerializedName("intHomeScore")
        val intHomeScore: String? = null,

        @field:SerializedName("intAwayScore")
        val intAwayScore: String? = null,

        @field:SerializedName("intHomeShots")
        val intHomeShots: String? = null,

        @field:SerializedName("intAwayShots")
        val intAwayShots: String? = null,

        @field:SerializedName("strHomeGoalDetails")
        val strHomeGoalDetails: String? = null,

        @field:SerializedName("strAwayGoalDetails")
        val strAwayGoalDetails: String? = null,

        @field:SerializedName("strHomeLineupGoalkeeper")
        val strHomeLineupGoalkeeper: String? = null,

        @field:SerializedName("strAwayLineupGoalkeeper")
        val strAwayLineupGoalkeeper: String? = null,

        @field:SerializedName("strHomeLineupDefense")
        val strHomeLineupDefense: String? = null,

        @field:SerializedName("strAwayLineupDefense")
        val strAwayLineupDefense: String? = null,

        @field:SerializedName("strHomeLineupMidfield")
        val strHomeLineupMidfield: String? = null,

        @field:SerializedName("strAwayLineupMidfield")
        val strAwayLineupMidfield: String? = null,

        @field:SerializedName("strHomeLineupForward")
        val strHomeLineupForward: String? = null,

        @field:SerializedName("strAwayLineupForward")
        val strAwayLineupForward: String? = null,

        @field:SerializedName("strHomeLineupSubstitutes")
        val strHomeLineupSubstitutes: String? = null,

        @field:SerializedName("strAwayLineupSubstitutes")
        val strAwayLineupSubstitutes: String? = null
) : Parcelable {

    fun getDateStringFromat(mode: String): String {
        var pattern = "yyyy-MM-dd"
        if (strTime != null) {
            pattern += " HH:mm"
        }

        val dateTime = "$dateEvent $strTime"

        val locale = Locale("in", "ID")
        val format = SimpleDateFormat(pattern, Locale.ENGLISH)
        val formatTujuan = SimpleDateFormat(mode, locale)

        format.timeZone = TimeZone.getTimeZone("GMT")
        formatTujuan.timeZone = TimeZone.getTimeZone("GMT+07:00")

        val date = format.parse(dateTime)
        return formatTujuan.format(date)
    }

    fun getDate(): Date {
        val pattern = "yyyy-MM-dd HH:mm"
        val locale = Locale("in", "ID")
        val format = SimpleDateFormat(pattern, Locale.ENGLISH)
        format.timeZone = TimeZone.getTimeZone("GMT+07:00")
        return format.parse(getDateStringFromat(pattern))
    }

    fun isPast(): Boolean {
        val now: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+07:00"))
        return now.time.after(getDate())
    }

    companion object {
        const val TABLE_NAME: String = "TABLE_MATCHES"
        const val ID: String = "ID_"
        const val ID_EVENT: String = "ID_EVENT"
        const val EVENT_NAME: String = "EVENT_NAME"
        const val EVENT_SPORT: String = "EVENT_SPORT"
        const val DATE_EVENT: String = "DATE_EVENT"
        const val TIME_EVENT: String = "TIME_EVENT"
        const val ID_HOME_TEAM: String = "ID_HOME_TEAM"
        const val ID_AWAY_TEAM: String = "ID_AWAY_TEAM"
        const val HOME_TEAM_NAME: String = "HOME_TEAM_NAME"
        const val AWAY_TEAM_NAME: String = "AWAY_TEAM_NAME"
        const val HOME_SCORE: String = "HOME_SCORE"
        const val AWAY_SCORE: String = "AWAY_SCORE"
        const val HOME_SHOTS: String = "HOME_SHOTS"
        const val AWAY_SHOTS: String = "AWAY_SHOTS"
        const val HOME_GOAL_DETAILS: String = "HOME_GOAL_DETAILS"
        const val AWAY_GOAL_DETAILS: String = "AWAY_GOAL_DETAILS"
        const val HOME_GOAL_KEEPER: String = "HOME_GOAL_KEEPER"
        const val AWAY_GOAL_KEEPER: String = "AWAY_GOAL_KEEPER"
        const val HOME_DEFENSE: String = "HOME_DEFENSE"
        const val AWAY_DEFENSE: String = "AWAY_DEFENSE"
        const val HOME_MID_FIELD: String = "HOME_MID_FIELD"
        const val AWAY_MID_FIELD: String = "AWAY_MID_FIELD"
        const val HOME_FORWARD: String = "HOME_FORWARD"
        const val AWAY_FORWARD: String = "AWAY_FORWARD"
        const val HOME_SUBSTITUTES: String = "HOME_SUBSTITUTES"
        const val AWAY_SUBSTITUTES: String = "AWAY_SUBSTITUTES"
    }
}