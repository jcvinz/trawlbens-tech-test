package com.calvin.trawlbenstechtest.data.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {

    private const val BASE_URL = "https://graphql.anilist.co"

    @Provides
    @Singleton
    fun provideApolloClient(httpClient: OkHttpClient): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BASE_URL)
            .okHttpClient(httpClient)
            .build()
    }

}