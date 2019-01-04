package com.example.trio.footballapps

import android.util.Log
import com.example.trio.footballapps.api.APIService
import com.example.trio.footballapps.model.TeamResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MatchDetailPresenter(
        private val view: MatchDetailView,
        private val apiService: APIService
) {
    val TAG = this::class.java.simpleName

    fun getHomeTeam(id: String) {
        val response: Observable<TeamResponse> = apiService.getTeam(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        response.subscribe(object : Observer<TeamResponse> {
            override fun onComplete() {
                Log.wtf(TAG, "complete home team request")
            }

            override fun onSubscribe(d: Disposable) {
                view.showHomeBadgeLoading()
            }

            override fun onNext(t: TeamResponse) {
                Log.wtf(TAG, "success home team request")
                view.showHomeTeam(t.teams!![0])
                view.hideHomeBadgeLoading()
            }

            override fun onError(e: Throwable) {
                Log.wtf(TAG, "error home team request")
                view.hideHomeBadgeLoading()
            }

        })
    }

    fun getAwayTeam(id: String) {
        val response: Observable<TeamResponse> = apiService.getTeam(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        response.subscribe(object : Observer<TeamResponse> {
            override fun onComplete() {
                Log.wtf(TAG, "complete away team request")
            }

            override fun onSubscribe(d: Disposable) {
                view.showAwayBadgeLoading()
            }

            override fun onNext(t: TeamResponse) {
                Log.wtf(TAG, "success away team request")
                view.showAwayTeam(t.teams!![0])
                view.hideAwayBadgeLoading()
            }

            override fun onError(e: Throwable) {
                Log.wtf(TAG, "error away team request")
                view.hideAwayBadgeLoading()
            }

        })
    }
}