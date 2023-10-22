package com.calvin.trawlbenstechtest.data.source

import androidx.room.Room
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.calvin.GetAnimeCardListQuery
import com.calvin.GetAnimeDetailQuery
import com.calvin.GetGenreListQuery
import com.calvin.trawlbenstechtest.domain.util.ResultState
import com.calvin.type.MediaSeason
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AnimeDataSource @Inject constructor(
    private val client: ApolloClient,
    private val dao: FavoriteAnimeDao
) {

    suspend fun getGenreList(): ResultState<List<String?>?> {
        val response = client.query(GetGenreListQuery()).execute()
        return if (response.hasErrors()) {
            ResultState.Failed(Exception("Something Went Wrong"))
        } else {
            ResultState.Success(response.data?.GenreCollection)
        }
    }

    suspend fun getAnimeCardList(
        page: Optional<Int?>,
        perPage: Optional<Int?>,
        genre: Optional<List<String?>>,
        season: Optional<MediaSeason?>,
        search: Optional<String?>
    ): ResultState<GetAnimeCardListQuery.Page?> {
        val response =
            client.query(GetAnimeCardListQuery(page, perPage, genre, season, search)).execute()
        return if (response.hasErrors()) {
            ResultState.Failed(Exception("Something Went Wrong"))
        } else {
            ResultState.Success(response.data?.Page)
        }
    }

    suspend fun getAnimeDetailList(id: Optional<Int?>): ResultState<GetAnimeDetailQuery.Media?> {
        val response = client.query(GetAnimeDetailQuery(id)).execute()
        return if (response.hasErrors()) {
            ResultState.Failed(Exception("Something Went Wrong"))
        } else {
            ResultState.Success(response.data?.Media)
        }
    }

    suspend fun getFavoriteAnimeList(): ResultState<List<FavoriteAnime>> {
        return try {
            val result = dao.getAll()
            ResultState.Success(result)
        } catch (e: Exception) {
            ResultState.Failed(e)
        }
    }

    suspend fun getFavoriteAnime(id: Int): ResultState<FavoriteAnime> {
        return try {
            val result = dao.getById(id)
            ResultState.Success(result)
        } catch (e: Exception) {
            ResultState.Failed(e)
        }
    }

    suspend fun addToFavorite(data: FavoriteAnime): ResultState<String> {
        return try {
            dao.insert(data)
            ResultState.Success("Added")
        } catch (e: Exception) {
            ResultState.Failed(e)
        }
    }

    suspend fun deleteFromFavorite(id: Int): ResultState<String> {
        return try {
            dao.deleteById(id)
            ResultState.Success("Deleted")
        } catch (e: Exception) {
            ResultState.Failed(e)
        }
    }

}