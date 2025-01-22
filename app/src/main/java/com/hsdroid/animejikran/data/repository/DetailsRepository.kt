package com.hsdroid.animejikran.data.repository

import com.hsdroid.animejikran.data.network.APIService
import com.hsdroid.animejikran.model.Characters
import com.hsdroid.animejikran.model.Details
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailsRepository @Inject constructor(private val apiService: APIService) {
    fun callAnimeDetails(id: String): Flow<Details> = flow {
        emit(apiService.getAnimeDetails(id))
    }.flowOn(Dispatchers.IO)

    fun callAnimeCharacters(id: String): Flow<Characters> = flow {
        emit(apiService.getCharacters(id))
    }.flowOn(Dispatchers.IO)
}