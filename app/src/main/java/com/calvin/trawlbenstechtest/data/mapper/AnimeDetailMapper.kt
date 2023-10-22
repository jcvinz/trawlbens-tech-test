package com.calvin.trawlbenstechtest.data.mapper

import com.calvin.GetAnimeDetailQuery
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeDetailUiModel

fun GetAnimeDetailQuery.Media?.toAnimeDetailModel(): AnimeDetailUiModel {
    this?.let {
        return AnimeDetailUiModel(
            title = title?.english.toString(),
            coverImage = coverImage?.extraLarge.toString(),
            bannerImage = bannerImage.toString(),
            siteUrl = siteUrl.toString(),
            description = description.toString()
        )
    }
    return AnimeDetailUiModel(
        title = "",
        coverImage = "",
        bannerImage = "",
        siteUrl = "",
        description = ""
    )
}