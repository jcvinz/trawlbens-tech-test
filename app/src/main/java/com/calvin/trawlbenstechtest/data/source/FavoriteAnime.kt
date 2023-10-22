package com.calvin.trawlbenstechtest.data.source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_anime")
data class FavoriteAnime(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "cover_image") val coverImage: String,
    @ColumnInfo(name = "is_favorite") val isFav: Boolean
)