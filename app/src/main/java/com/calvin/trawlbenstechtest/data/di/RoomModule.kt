package com.calvin.trawlbenstechtest.data.di

import android.content.Context
import androidx.room.Room
import com.calvin.trawlbenstechtest.data.source.AppDatabase
import com.calvin.trawlbenstechtest.data.source.FavoriteAnimeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): FavoriteAnimeDao {
        return appDatabase.favoriteAnimeDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "pockenime"
        ).build()
    }

}