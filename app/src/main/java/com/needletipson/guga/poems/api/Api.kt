package com.needletipson.guga.poems.api

import com.needletipson.guga.poems.Count
import com.needletipson.guga.poems.Poems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface Api {
    val BASE_URL: String
        get() = "http://poemsapi.ga/public/api/poem/"

    @GET
    fun getPoems(@Url url: String): Call<List<Poems>>

    @GET
    fun getPoemsCount(@Url url: String): Call<List<Count>>
}