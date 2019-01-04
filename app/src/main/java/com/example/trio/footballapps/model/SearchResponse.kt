package com.example.trio.footballapps.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
        @field:SerializedName("event")
        val events: List<EventsItem>? = null
)