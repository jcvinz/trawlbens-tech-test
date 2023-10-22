package com.calvin.trawlbenstechtest.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeDetailUiModel
import com.calvin.trawlbenstechtest.domain.usecase.AnimeUseCase
import com.calvin.trawlbenstechtest.domain.util.ViewState
import com.calvin.trawlbenstechtest.domain.util.onFailure
import com.calvin.trawlbenstechtest.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(private val animeUseCase: AnimeUseCase) :
    ViewModel() {

    private val _animeDetailViewState =
        MutableStateFlow<ViewState<AnimeDetailUiModel>>(ViewState.Loading(true))
    val animeDetailUiModel = _animeDetailViewState.asStateFlow()

    private val _isFavoriteViewState = MutableStateFlow<ViewState<Boolean?>>(ViewState.Loading(true))
    val isFavoriteViewState = _isFavoriteViewState.asStateFlow()

    private val _favoriteViewState = MutableStateFlow<ViewState<String>>(ViewState.Loading(true))
    val favoriteViewState = _favoriteViewState.asStateFlow()

    fun getAnimeDetail(id: Int) = viewModelScope.launch {
        animeUseCase.getAnimeDetail(id)
            .onSuccess {
                _animeDetailViewState.value = ViewState.Success(it)
            }
            .onFailure {
                _animeDetailViewState.value = ViewState.Error(it)
            }
    }

    fun isFavoriteAnime(id: Int) = viewModelScope.launch {
        animeUseCase.isFavoriteAnime(id)
            .onSuccess {
                _isFavoriteViewState.value = ViewState.Success(it)
            }
            .onFailure {
                _isFavoriteViewState.value = ViewState.Error(it)
            }
    }

    fun addToFavorite(id: Int, title: String, coverImage: String, isFavorite: Boolean) =
        viewModelScope.launch {
            animeUseCase.addToFavorite(id, title, coverImage, isFavorite)
                .onSuccess {
                    _favoriteViewState.value = ViewState.Success(it)
                }
                .onFailure {
                    _favoriteViewState.value = ViewState.Error(it)
                }
        }

    fun deleteFromFavorite(id: Int) = viewModelScope.launch {
        animeUseCase.deleteFromFavorite(id)
            .onSuccess {
                _favoriteViewState.value = ViewState.Success(it)
            }
            .onFailure {
                _favoriteViewState.value = ViewState.Error(it)
            }
    }
}