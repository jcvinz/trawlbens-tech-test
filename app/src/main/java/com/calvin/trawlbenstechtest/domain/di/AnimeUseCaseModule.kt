package com.calvin.trawlbenstechtest.domain.di

import com.calvin.trawlbenstechtest.domain.interactor.AnimeInteractor
import com.calvin.trawlbenstechtest.domain.usecase.AnimeUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class AnimeUseCaseModule {

    @Binds
    abstract fun bindAnimeUseCase(animeInteractor: AnimeInteractor): AnimeUseCase

}