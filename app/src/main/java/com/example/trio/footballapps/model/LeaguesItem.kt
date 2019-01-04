package com.example.trio.footballapps.model

data class LeaguesItem(
        val strLeague: String? = null,
        val strSport: String? = null,
        val idLeague: String? = null
) {
    override fun toString(): String {
        return this.strLeague ?: ""
    }
}
