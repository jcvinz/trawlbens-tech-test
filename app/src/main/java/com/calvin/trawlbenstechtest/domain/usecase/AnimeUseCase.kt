package com.calvin.trawlbenstechtest.domain.usecase

import androidx.paging.PagingData
import com.calvin.trawlbenstechtest.data.source.FavoriteAnime
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeCardUiModel
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeDetailUiModel
import com.calvin.trawlbenstechtest.domain.util.ResultState
import com.calvin.type.MediaSeason
import kotlinx.coroutines.flow.Flow

interface AnimeUseCase {

    suspend fun getGenreList(): List<String>

    suspend fun getAnimeList(
        genre: List<String?>?,
        season: MediaSeason?,
        search: String?
    ): Flow<PagingData<AnimeCardUiModel>>

    suspend fun getAnimeDetail(id: Int): ResultState<AnimeDetailUiModel>

    suspend fun getFavoriteAnimeList(): ResultState<List<AnimeCardUiModel>>

    suspend fun isFavoriteAnime(id: Int): ResultState<Boolean?>

    suspend fun addToFavorite(id: Int, title: String, coverImage: String, isFavorite: Boolean): ResultState<String>

    suspend fun deleteFromFavorite(id: Int): ResultState<String>
}