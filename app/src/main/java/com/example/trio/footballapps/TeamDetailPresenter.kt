package com.example.trio.footballapps

import android.util.Log
import com.example.trio.footballapps.api.APIService
import com.example.trio.footballapps.model.PlayerResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TeamDetailPresenter(
        private val view: TeamDetailView,
        private val apiService: APIService
) {
    val TAG = this::class.java.simpleName

    fun getPlayers(id: String) {
        val response: Observable<PlayerResponse> = apiService.getPlayers(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        response.subscribe(object : Observer<PlayerResponse> {
            override fun onComplete() {
                Log.wtf(TAG, "complete players request")
            }

            override fun onSubscribe(d: Disposable) {
                view.showLoading()
            }

            override fun onNext(t: PlayerResponse) {
                Log.wtf(TAG, "success players request")
                view.showPlayers(t.player!!)
                view.hideLoading()
            }

            override fun onError(e: Throwable) {
                Log.wtf(TAG, "error players request")
                view.hideLoading()
            }

        })
    }
}