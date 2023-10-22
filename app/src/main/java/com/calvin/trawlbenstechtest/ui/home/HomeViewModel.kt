package com.calvin.trawlbenstechtest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeCardUiModel
import com.calvin.trawlbenstechtest.domain.usecase.AnimeUseCase
import com.calvin.trawlbenstechtest.domain.util.ViewState
import com.calvin.type.MediaSeason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val animeUseCase: AnimeUseCase) : ViewModel() {

    private val _animeListViewState =
        MutableStateFlow<ViewState<PagingData<AnimeCardUiModel>>>(ViewState.Loading(true))
    val animeListViewState = _animeListViewState.asStateFlow()

    fun getAnimeCardList(genre: List<String?>?, season: MediaSeason?, search: String?) =
        viewModelScope.launch {
            setAnimeCardPagingData(animeUseCase.getAnimeList(genre, season, search))
        }

    private suspend fun setAnimeCardPagingData(request: Flow<PagingData<AnimeCardUiModel>>) {
        request.cachedIn(viewModelScope).catch {
            _animeListViewState.value = ViewState.Error(Exception(it.message))
        }.collect {
            _animeListViewState.value = ViewState.Success(it)
        }
    }
}