package com.calvin.trawlbenstechtest.domain.interactor

import androidx.paging.PagingData
import com.calvin.trawlbenstechtest.domain.repository.AnimeRepository
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeCardUiModel
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeDetailUiModel
import com.calvin.trawlbenstechtest.domain.usecase.AnimeUseCase
import com.calvin.trawlbenstechtest.domain.util.ResultState
import com.calvin.type.MediaSeason
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeInteractor @Inject constructor(private val animeRepository: AnimeRepository) :
    AnimeUseCase {

    override suspend fun getGenreList(): List<String> {
        return when (val result = animeRepository.getGenreList()) {
            is ResultState.Success -> {
                result.data
            }

            is ResultState.Failed -> {
                mutableListOf()
            }
        }
    }

    override suspend fun getAnimeList(
        genre: List<String?>?,
        season: MediaSeason?,
        search: String?
    ): Flow<PagingData<AnimeCardUiModel>> =
        animeRepository.getAnimeList(genre, season, search)

    override suspend fun getAnimeDetail(id: Int): ResultState<AnimeDetailUiModel> =
        animeRepository.getAnimeDetail(id)

    override suspend fun getFavoriteAnimeList(): ResultState<List<AnimeCardUiModel>> =
        animeRepository.getFavoriteAnimeList()

    override suspend fun isFavoriteAnime(id: Int): ResultState<Boolean?> =
        animeRepository.isFavoriteAnime(id)

    override suspend fun addToFavorite(
        id: Int,
        title: String,
        coverImage: String,
        isFavorite: Boolean
    ): ResultState<String> =
        animeRepository.addToFavorite(id, title, coverImage, isFavorite)


    override suspend fun deleteFromFavorite(id: Int): ResultState<String> =
        animeRepository.deleteFromFavorite(id)
}