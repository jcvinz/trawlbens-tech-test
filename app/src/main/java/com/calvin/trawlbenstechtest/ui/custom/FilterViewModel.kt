package com.calvin.trawlbenstechtest.ui.custom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calvin.trawlbenstechtest.domain.usecase.AnimeUseCase
import com.calvin.trawlbenstechtest.domain.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(private val animeUseCase: AnimeUseCase) : ViewModel() {

    private val _genreList =
        MutableStateFlow<ViewState<List<String>>>(ViewState.Loading(true))
    val genreList = _genreList.asStateFlow()

    fun getGenreList() = viewModelScope.launch {
        _genreList.value = ViewState.Success(animeUseCase.getGenreList())
    }
}