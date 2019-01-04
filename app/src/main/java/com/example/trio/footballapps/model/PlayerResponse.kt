package com.example.trio.footballapps.model

import com.google.gson.annotations.SerializedName

data class PlayerResponse(

        @field:SerializedName("player")
        val player: List<PlayerItem>? = null
)