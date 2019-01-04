package com.example.trio.footballapps.api

import com.example.trio.footballapps.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    companion object {
        fun getService(): APIService {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL + "/api/v1/json/" + BuildConfig.TSDB_API_KEY + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            return retrofit.create(APIService::class.java)
        }
    }
}