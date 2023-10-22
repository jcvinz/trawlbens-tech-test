package com.calvin.trawlbenstechtest.data.di

import com.calvin.trawlbenstechtest.data.repository.AnimeRepositoryImpl
import com.calvin.trawlbenstechtest.domain.repository.AnimeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AnimeModule {

    @Binds
    abstract fun bindAnimeRepository(animeRepositoryImpl: AnimeRepositoryImpl): AnimeRepository

}