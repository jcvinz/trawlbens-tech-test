package com.calvin.trawlbenstechtest.domain.uimodel

import androidx.recyclerview.widget.DiffUtil

data class AnimeCardUiModel(

    val id: Int,
    val title: String,
    val coverImage: String

)

object AnimeDiffUtil : DiffUtil.ItemCallback<AnimeCardUiModel>() {

    override fun areContentsTheSame(oldItem: AnimeCardUiModel, newItem: AnimeCardUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: AnimeCardUiModel, newItem: AnimeCardUiModel): Boolean {
        return oldItem.id == newItem.id
    }

}

