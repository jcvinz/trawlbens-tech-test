package com.calvin.trawlbenstechtest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.api.Optional
import com.calvin.trawlbenstechtest.data.mapper.toAnimeCardUiModel
import com.calvin.trawlbenstechtest.data.mapper.toAnimeDetailModel
import com.calvin.trawlbenstechtest.data.source.AnimeDataSource
import com.calvin.trawlbenstechtest.data.source.FavoriteAnime
import com.calvin.trawlbenstechtest.domain.pagination.AnimePagingSource
import com.calvin.trawlbenstechtest.domain.repository.AnimeRepository
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeCardUiModel
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeDetailUiModel
import com.calvin.trawlbenstechtest.domain.uimodel.PagingDataUiModel
import com.calvin.trawlbenstechtest.domain.util.ResultState
import com.calvin.trawlbenstechtest.domain.util.onFailure
import com.calvin.trawlbenstechtest.domain.util.onSuccess
import com.calvin.type.MediaSeason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(private val source: AnimeDataSource) :
    AnimeRepository {

    override suspend fun getGenreList(): ResultState<List<String>> = withContext(Dispatchers.IO) {
        return@withContext when (val result = source.getGenreList()) {
            is ResultState.Success -> {
                ResultState.Success(result.data!!.map { it.toString() })
            }

            is ResultState.Failed -> {
                ResultState.Failed(result.error)
            }
        }

    }

    override suspend fun getAnimeList(
        genre: List<String?>?,
        season: MediaSeason?,
        search: String?
    ): Flow<PagingData<AnimeCardUiModel>> =
        Pager(
            PagingConfig(
                pageSize = 10,
                prefetchDistance = 1,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                AnimePagingSource { loadParams ->
                    val animeList = mutableListOf<AnimeCardUiModel>()
                    var hasNextPage: Boolean? = null

                    source.getAnimeCardList(
                        Optional.presentIfNotNull(loadParams.key),
                        Optional.presentIfNotNull(loadParams.loadSize),
                        Optional.presentIfNotNull(genre),
                        Optional.presentIfNotNull(season),
                        Optional.presentIfNotNull(search)
                    ).onSuccess { data ->
                        data?.media?.map { it?.toAnimeCardUiModel() }
                            ?.let { animeList.addAll(it.filterNotNull()) }
                        hasNextPage = data?.pageInfo?.hasNextPage
                    }.onFailure {
                        println(it)
                    }
                    PagingDataUiModel(
                        data = animeList,
                        hasNextPage = hasNextPage
                    )
                }
            }
        ).flow

    override suspend fun getAnimeDetail(id: Int): ResultState<AnimeDetailUiModel> =
        withContext(Dispatchers.IO) {
            return@withContext when (val result =
                source.getAnimeDetailList(Optional.presentIfNotNull(id))) {
                is ResultState.Success -> {
                    ResultState.Success(result.data.toAnimeDetailModel())
                }

                is ResultState.Failed -> {
                    ResultState.Failed(result.error)
                }
            }
        }

    override suspend fun getFavoriteAnimeList(): ResultState<List<AnimeCardUiModel>> =
        withContext(Dispatchers.IO) {
            return@withContext when (val result = source.getFavoriteAnimeList()) {
                is ResultState.Success -> {
                    ResultState.Success(result.data.map { it.toAnimeCardUiModel() })
                }

                is ResultState.Failed -> {
                    ResultState.Failed(result.error)
                }
            }
        }

    override suspend fun isFavoriteAnime(id: Int): ResultState<Boolean?> =
        withContext(Dispatchers.IO) {
            return@withContext when (val result = source.getFavoriteAnime(id)) {
                is ResultState.Success -> {
                    if (result.data != null) {
                        ResultState.Success(result.data.isFav)
                    } else {
                        ResultState.Success(false)
                    }
                }

                is ResultState.Failed -> {
                    ResultState.Failed(result.error)
                }
            }
        }

    override suspend fun addToFavorite(
        id: Int,
        title: String,
        coverImage: String,
        isFavorite: Boolean
    ): ResultState<String> =
        withContext(Dispatchers.IO) {
            return@withContext when (val result = source.addToFavorite(
                FavoriteAnime(
                    id = id,
                    title = title,
                    coverImage = coverImage,
                    isFav = isFavorite
                )
            )) {
                is ResultState.Success -> {
                    ResultState.Success(result.data)
                }

                is ResultState.Failed -> {
                    ResultState.Failed(result.error)
                }
            }
        }


    override suspend fun deleteFromFavorite(id: Int): ResultState<String> =
        withContext(Dispatchers.IO) {
            return@withContext when (val result = source.deleteFromFavorite(id)) {
                is ResultState.Success -> {
                    ResultState.Success(result.data)
                }

                is ResultState.Failed -> {
                    ResultState.Failed(result.error)
                }
            }
        }
}
