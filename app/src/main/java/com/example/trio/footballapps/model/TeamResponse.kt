package com.example.trio.footballapps.model

import com.google.gson.annotations.SerializedName

data class TeamResponse(

        @field:SerializedName("teams")
        val teams: List<TeamsItem>? = null
)