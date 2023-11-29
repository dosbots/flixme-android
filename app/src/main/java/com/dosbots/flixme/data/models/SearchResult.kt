package com.dosbots.flixme.data.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "searchResults",
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movieId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["movieId", "query"]
)
data class SearchResult(
    val movieId: Int,
    val query: String,
    val page: Int,
    val createdAt: Long
)

data class SearchResultWithMovieData(
    val movieId: Int,
    val movieTitle: String,
    val movieOverview: String,
    val movieImage: String,
    val searchQuery: String,
    val responsePage: Int,
    val searchedAt: Long
)