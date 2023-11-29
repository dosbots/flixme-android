package com.dosbots.flixme.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.firebase.firestore.Exclude

@Entity
data class MyMoviesList(
    @PrimaryKey val id: String,
    val title: String,
    val createdAt: Long,
    val editedAt: Long,
    val owners: List<String>,

    @Exclude @get:Exclude
    val synced: Boolean = false
)

data class MyMoviesListWithMovies(
    @Embedded val list: MyMoviesList,

    @Relation(
        entity = MyMoviesListItem::class,
        entityColumn = "listId",
        parentColumn = "id"
    )
    val items: List<MyListItemWithMovie>
)

data class MyListItemWithMovie(
    @Embedded val listItem: MyMoviesListItem,

    @Relation(
        entity = Movie::class,
        entityColumn = "id",
        parentColumn = "movieId"
    )
    val movie: Movie
)

data class MyMoviesListWithMoviesItem(
    val movieTitle: String,
    val moviePoster: String,
    val movieDescription: String,
    val watched: Boolean,
    val dateAdded: Long
)