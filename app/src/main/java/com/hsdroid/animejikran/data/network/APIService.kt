package com.hsdroid.animejikran.data.network

import com.hsdroid.animejikran.model.Characters
import com.hsdroid.animejikran.model.Details
import com.hsdroid.animejikran.model.TopAnime
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    @GET("top/anime")
    suspend fun getTopAnime(): TopAnime

    @GET("anime/{anime_id}")
    suspend fun getAnimeDetails(@Path("anime_id") id: String): Details

    @GET("characters/{anime_id}")
    suspend fun getCharacters(@Path("anime_id") id: String): Characters
}