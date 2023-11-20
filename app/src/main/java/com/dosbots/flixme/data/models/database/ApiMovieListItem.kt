package com.dosbots.flixme.data.models.database

import androidx.room.Entity
import androidx.room.ForeignKey
import com.dosbots.flixme.data.models.Movie

/**
 * Represents an item in a movie list created by the API.
 * These lists doesn't belong to the user, they are created by TMDB
 */
@Entity(
    tableName = "apiMovieListItems",
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movieId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["movieId", "listName"]
)
data class ApiMovieListItem(
    val movieId: Int,
    val listName: String,
    val page: Int,
    val createdAt: Long
) {

    companion object {
        const val POPULAR_MOVIES_LIST = "popular"
    }
}