package com.example.trio.footballapps.menu.team

import android.util.Log
import com.example.trio.footballapps.api.APIService
import com.example.trio.footballapps.model.LeagueResponse
import com.example.trio.footballapps.model.TeamResponse
import com.example.trio.footballapps.model.TeamsItem
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TeamPresenter(
        private val view: TeamView,
        private val apiService: APIService
) {
    val TAG = TeamPresenter::class.java.simpleName

    fun getAllLeagues() {
        val response: Observable<LeagueResponse> = apiService.getAllLeagues()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        response.subscribe(object : Observer<LeagueResponse> {
            override fun onComplete() {
                Log.wtf(TAG, "complete leagues request")
            }

            override fun onSubscribe(d: Disposable) {
                view.showLoading()
            }

            override fun onNext(t: LeagueResponse) {
                Log.wtf(TAG, "success leagues request")
                view.showLeagues(t.leagues!!)
                view.hideLoading()
            }

            override fun onError(e: Throwable) {
                Log.wtf(TAG, "error leagues request")
            }

        })
    }

    fun getTeams(id: String) {
        val response: Observable<TeamResponse> = apiService.getTeams(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        response.subscribe(object : Observer<TeamResponse> {
            override fun onComplete() {
                Log.wtf(TAG, "complete teams request")
            }

            override fun onSubscribe(d: Disposable) {
                view.showLoading()
            }

            override fun onNext(t: TeamResponse) {
                Log.wtf(TAG, "success teams request")
                val data: MutableList<TeamsItem> = mutableListOf()
                if (t.teams != null) data.addAll(t.teams)
                view.showTeams(data)
                view.hideLoading()
            }

            override fun onError(e: Throwable) {
                Log.wtf(TAG, "error teams request")
            }

        })
    }

    fun searchTeams(param: String) {
        val response: Observable<TeamResponse> = apiService.searchTeams(param)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        response.subscribe(object : Observer<TeamResponse> {
            override fun onComplete() {
                Log.wtf(TAG, "complete search teams request")
            }

            override fun onSubscribe(d: Disposable) {
                view.showLoading()
            }

            override fun onNext(t: TeamResponse) {
                Log.wtf(TAG, "success search teams request")
                val data: MutableList<TeamsItem> = mutableListOf()
                if (t.teams != null) data.addAll(t.teams)
                view.showSearched(data)
                view.hideLoading()
            }

            override fun onError(e: Throwable) {
                Log.wtf(TAG, "error search teams request")
            }

        })
    }
}