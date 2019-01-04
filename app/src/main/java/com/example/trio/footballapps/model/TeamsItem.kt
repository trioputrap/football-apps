package com.example.trio.footballapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeamsItem(
        var id: Int? = null,

        @field:SerializedName("idTeam")
        val idTeam: String? = null,

        @field:SerializedName("strTeam")
        val strTeam: String? = null,

        @field:SerializedName("intFormedYear")
        val intFormedYear: String? = null,

        @field:SerializedName("strDescriptionEN")
        val strDescriptionEN: String? = null,

        @field:SerializedName("strStadium")
        val strStadium: String? = null,

        @field:SerializedName("strTeamBadge")
        val strTeamBadge: String? = null

) : Parcelable {

    companion object {
        const val TABLE_NAME: String = "TABLE_TEAM"
        const val ID: String = "ID_"
        const val ID_TEAM: String = "ID_TEAM"
        const val TEAM_NAME: String = "TEAM_NAME"
        const val FORMED_YEAR: String = "FORMED_YEAR"
        const val DESCRIPTION: String = "DESCRIPTION"
        const val STADIUM: String = "STADIUM"
        const val BADGE: String = "BADGE"
    }
}