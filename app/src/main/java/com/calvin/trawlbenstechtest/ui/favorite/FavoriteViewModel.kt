package com.calvin.trawlbenstechtest.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeCardUiModel
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
class FavoriteViewModel @Inject constructor(private val animeUseCase: AnimeUseCase) : ViewModel() {

    private val _favoriteAnimeListViewState =
        MutableStateFlow<ViewState<List<AnimeCardUiModel>>>(ViewState.Loading(true))
    val favoriteAnimeListViewState = _favoriteAnimeListViewState.asStateFlow()

    fun getFavoriteAnimeList() = viewModelScope.launch {
        animeUseCase.getFavoriteAnimeList()
            .onSuccess {
                _favoriteAnimeListViewState.value = ViewState.Success(it)
            }
            .onFailure {
                _favoriteAnimeListViewState.value = ViewState.Error(it)
            }
    }
}