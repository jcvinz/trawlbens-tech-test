package com.calvin.trawlbenstechtest.data.mapper

import com.calvin.GetAnimeCardListQuery
import com.calvin.trawlbenstechtest.data.source.FavoriteAnime
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeCardUiModel

fun GetAnimeCardListQuery.Medium.toAnimeCardUiModel(): AnimeCardUiModel {
    return AnimeCardUiModel(
        id = id,
        title = title?.english ?: "-",
        coverImage = coverImage?.extraLarge.toString()
    )
}

fun FavoriteAnime.toAnimeCardUiModel(): AnimeCardUiModel {
    return AnimeCardUiModel(
        id = id,
        title = title,
        coverImage = coverImage
    )
}