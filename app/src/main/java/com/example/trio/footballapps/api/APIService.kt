package com.example.trio.footballapps.api

import com.example.trio.footballapps.model.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("all_leagues.php")
    fun getAllLeagues(): Observable<LeagueResponse>

    @GET("eventsnextleague.php")
    fun getNextMatches(@Query("id") id: String): Observable<MatchResponse>

    @GET("eventspastleague.php")
    fun getLastMatches(@Query("id") id: String): Observable<MatchResponse>

    @GET("lookup_all_teams.php")
    fun getTeams(@Query("id") id: String): Observable<TeamResponse>

    @GET("lookupteam.php")
    fun getTeam(@Query("id") id: String): Observable<TeamResponse>

    @GET("lookup_all_players.php")
    fun getPlayers(@Query("id") id: String): Observable<PlayerResponse>

    @GET("searchevents.php")
    fun searchMatches(@Query("e") param: String): Observable<SearchResponse>

    @GET("searchteams.php")
    fun searchTeams(@Query("t") param: String): Observable<TeamResponse>
}