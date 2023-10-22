package com.calvin.trawlbenstechtest.data.source

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteAnime::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteAnimeDao(): FavoriteAnimeDao
}