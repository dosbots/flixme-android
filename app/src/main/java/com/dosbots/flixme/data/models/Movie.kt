package com.dosbots.flixme.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    @field:Json(name = "release_date") val releaseDate: String,
    @field:Json(name = "poster_path") val posterPath: String,
    @field:Json(name = "backdrop_path") val backdropPath: String?
)