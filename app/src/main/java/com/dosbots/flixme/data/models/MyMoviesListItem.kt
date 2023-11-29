package com.dosbots.flixme.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude

@Entity(
    primaryKeys = ["listId", "movieId"],
    foreignKeys = [
        ForeignKey(
            entity = MyMoviesList::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("listId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Movie::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movieId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MyMoviesListItem(
    val listId: String,
    val movieId: Int,
    val watched: Boolean,
    val addedAt: Long,

    @Exclude @get:Exclude
    val synced: Boolean = false
)