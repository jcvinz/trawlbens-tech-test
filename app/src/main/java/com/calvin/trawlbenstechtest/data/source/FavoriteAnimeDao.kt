package com.calvin.trawlbenstechtest.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FavoriteAnimeDao {

    @Transaction
    @Query("SELECT * FROM favorite_anime")
    suspend fun getAll(): List<FavoriteAnime>

    @Transaction
    @Query("SELECT * FROM favorite_anime WHERE id = :id")
    suspend fun getById(id: Int): FavoriteAnime

    @Insert
    suspend fun insert(data: FavoriteAnime)

    @Transaction
    @Query("DELETE FROM favorite_anime WHERE id = :id")
    suspend fun deleteById(id: Int)

}