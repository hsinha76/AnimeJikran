package com.hsdroid.animejikran.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsdroid.animejikran.data.repository.DetailsRepository
import com.hsdroid.animejikran.model.Characters
import com.hsdroid.animejikran.model.Details
import com.hsdroid.animejikran.utils.APIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val detailsRepository: DetailsRepository) :
    ViewModel() {

    private val _animeDetailsResponse: MutableStateFlow<APIState<Details>> =
        MutableStateFlow(APIState.EMPTY)
    val animeDetailsResponse: StateFlow<APIState<Details>> = _animeDetailsResponse.asStateFlow()

    private val _animeCharactersResponse: MutableStateFlow<APIState<Characters>> =
        MutableStateFlow(APIState.EMPTY)
    val animeCharactersResponse: StateFlow<APIState<Characters>> =
        _animeCharactersResponse.asStateFlow()

    fun getAnimeDetails(id: String) = viewModelScope.launch {
        detailsRepository.callAnimeDetails(id)
            .onStart {
                _animeDetailsResponse.value = APIState.LOADING
            }.catch {
                _animeDetailsResponse.value = APIState.FAILURE(it)
            }.collect {
                _animeDetailsResponse.value = APIState.SUCCESS(it)
            }
    }

    fun getAnimeCharacters(id: String) = viewModelScope.launch {
        detailsRepository.callAnimeCharacters(id)
            .onStart {
                _animeCharactersResponse.value = APIState.LOADING
            }.catch {
                _animeCharactersResponse.value = APIState.FAILURE(it)
            }.collect {
                _animeCharactersResponse.value = APIState.SUCCESS(it)
            }
    }
}