package com.calvin.trawlbenstechtest.domain.repository

import androidx.paging.PagingData
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeCardUiModel
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeDetailUiModel
import com.calvin.trawlbenstechtest.domain.util.ResultState
import com.calvin.type.MediaSeason
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    suspend fun getGenreList(): ResultState<List<String>>

    suspend fun getAnimeList(
        genre: List<String?>?,
        season: MediaSeason?,
        search: String?
    ): Flow<PagingData<AnimeCardUiModel>>

    suspend fun getAnimeDetail(id: Int): ResultState<AnimeDetailUiModel>

    suspend fun getFavoriteAnimeList(): ResultState<List<AnimeCardUiModel>>

    suspend fun isFavoriteAnime(id: Int): ResultState<Boolean?>

    suspend fun addToFavorite(
        id: Int,
        title: String,
        coverImage: String,
        isFavorite: Boolean
    ): ResultState<String>

    suspend fun deleteFromFavorite(id: Int): ResultState<String>

}