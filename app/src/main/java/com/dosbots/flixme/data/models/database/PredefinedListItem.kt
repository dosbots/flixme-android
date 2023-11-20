package com.dosbots.flixme.data.models.database

import androidx.room.Entity
import androidx.room.ForeignKey
import com.dosbots.flixme.data.models.Movie

/**
 * Represents an item in a movie list created by the API.
 *
 * These lists don't belong to the user, they are created by TMDB, and
 * are a predefined set of movies that every user can see, categorized by
 * a certain criteria: popular, most rated, upcoming, etc.
 */
@Entity(
    tableName = "predefinedListsItems",
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
data class PredefinedListItem(
    val movieId: Int,
    val listName: String,
    val page: Int,
    val createdAt: Long
) {

    companion object {
        const val POPULAR_MOVIES_LIST = "popular"
    }
}