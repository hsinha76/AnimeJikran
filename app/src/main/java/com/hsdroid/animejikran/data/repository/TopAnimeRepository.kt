package com.hsdroid.animejikran.data.repository

import com.hsdroid.animejikran.data.network.APIService
import com.hsdroid.animejikran.model.TopAnime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TopAnimeRepository @Inject constructor(private val apiService: APIService) {
    fun callTopAnime(): Flow<TopAnime> = flow {
        emit(apiService.getTopAnime())
    }.flowOn(Dispatchers.IO)
}