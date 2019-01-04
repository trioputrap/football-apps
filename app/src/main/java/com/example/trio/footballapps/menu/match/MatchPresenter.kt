package com.example.trio.footballapps.menu.match

import android.util.Log
import com.example.trio.footballapps.api.APIService
import com.example.trio.footballapps.model.EventsItem
import com.example.trio.footballapps.model.LeagueResponse
import com.example.trio.footballapps.model.MatchResponse
import com.example.trio.footballapps.model.SearchResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MatchPresenter(
        private val view: MatchView,
        private val apiService: APIService
) {
    val TAG = MatchPresenter::class.java.simpleName
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
                view.showLeagues(t.leagues!!.filter { league -> league.strSport == "Soccer" })
                view.hideLoading()
            }

            override fun onError(e: Throwable) {
                Log.wtf(TAG, "error leagues request")
            }

        })
    }

    //testing
    fun getNextMatches(id: String, scheduler: Scheduler = AndroidSchedulers.mainThread()) {
        val response: Observable<MatchResponse> = apiService.getNextMatches(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(scheduler)
        response.subscribe(object : Observer<MatchResponse> {
            override fun onComplete() {
                //Log.wtf(TAG, "complete next matches request")
            }

            override fun onSubscribe(d: Disposable) {
                view.showLoading()
            }

            override fun onNext(t: MatchResponse) {
                //Log.wtf(TAG, "success next matches request id=" + id)
                val data: MutableList<EventsItem> = mutableListOf()
                if (t.events != null) data.addAll(t.events)
                view.showMatches(data)
                view.hideLoading()
            }

            override fun onError(e: Throwable) {
                //Log.wtf(TAG, "error next matches request")
            }

        })
    }

    fun getLastMatches(id: String) {
        val response: Observable<MatchResponse> = apiService.getLastMatches(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        response.subscribe(object : Observer<MatchResponse> {
            override fun onComplete() {
                Log.wtf(TAG, "complete last matches request")
            }

            override fun onSubscribe(d: Disposable) {
                view.showLoading()
            }

            override fun onNext(t: MatchResponse) {
                Log.wtf(TAG, "success last matches request id=" + id)
                val data: MutableList<EventsItem> = mutableListOf()
                if (t.events != null) data.addAll(t.events)
                view.showMatches(data)
                view.hideLoading()
            }

            override fun onError(e: Throwable) {
                Log.wtf(TAG, "error last matches request")
            }

        })
    }

    fun searchMatches(param: String) {
        val response: Observable<SearchResponse> = apiService.searchMatches(param)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        response.subscribe(object : Observer<SearchResponse> {
            override fun onComplete() {
                Log.wtf(TAG, "complete search matches request")
            }

            override fun onSubscribe(d: Disposable) {
                view.showLoading()
            }

            override fun onNext(t: SearchResponse) {
                Log.wtf(TAG, "success search matches request e=" + param + " leng = " + t.events?.size)
                val data: MutableList<EventsItem> = mutableListOf()
                if (t.events != null) data.addAll(t.events.filter { match -> match.strSport == "Soccer" })
                view.showSearched(data)
                view.hideLoading()
            }

            override fun onError(e: Throwable) {
                Log.wtf(TAG, "error search matches request")
            }

        })
    }
}