package com.hsdroid.animejikran.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsdroid.animejikran.data.repository.TopAnimeRepository
import com.hsdroid.animejikran.model.TopAnime
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
class HomeViewModel @Inject constructor(private val topAnimeRepository: TopAnimeRepository) :
    ViewModel() {

    private val _topAnimeResponse: MutableStateFlow<APIState<TopAnime>> =
        MutableStateFlow(APIState.EMPTY)
    val topAnimeResponse: StateFlow<APIState<TopAnime>> = _topAnimeResponse.asStateFlow()

    init {
        getTopAnime()
    }

    private fun getTopAnime() = viewModelScope.launch {
        topAnimeRepository.callTopAnime()
            .onStart {
                _topAnimeResponse.value = APIState.LOADING
            }.catch {
                _topAnimeResponse.value = APIState.FAILURE(it)
            }.collect {
                _topAnimeResponse.value = APIState.SUCCESS(it)
            }
    }
}