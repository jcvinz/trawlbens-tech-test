package com.calvin.trawlbenstechtest.domain.uimodel

data class PagingDataUiModel(
    val data: List<AnimeCardUiModel>,
    val hasNextPage: Boolean?
)
