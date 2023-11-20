package com.dosbots.flixme.data.models.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.dosbots.flixme.data.models.Movie

@Entity(
    tableName = "popularMovies",
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movieId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PopularMovie(
    @PrimaryKey val movieId: Int,
    val page: Int
)