package com.dosbots.flixme.data.dabase

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.PredefinedListItem

@Dao
abstract class MoviesDao {

    @Transaction
    open suspend fun saveApiMovieListItem(movies: List<Movie>, predefinedListsItems: List<PredefinedListItem>) {
        insertMovies(movies)
        insertPopularMovies(predefinedListsItems)
    }

    // Movies table methods

    @Query("SELECT * FROM movies WHERE id = :id")
    abstract suspend fun getMovieById(id: Int): Movie

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertMovies(movies: List<Movie>)

    // Predefined lists table methods

    @Query("SELECT m.* FROM movies m JOIN predefinedListsItems pm ON m.id = pm.movieId WHERE pm.listName = :listName ORDER BY pm.page")
    abstract fun getMoviesInPredefinedListPaginated(listName: String): PagingSource<Int, Movie>

    @Query("SELECT COUNT(*) FROM predefinedListsItems WHERE listName = :listName")
    abstract suspend fun countMoviesInPredefinedList(listName: String): Int

    @Query("SELECT * FROM predefinedListsItems WHERE listName = :listName ORDER BY createdAt LIMIT 1")
    abstract suspend fun getOldestApiMovieListItem(listName: String): PredefinedListItem?

    @Query("SELECT * FROM predefinedListsItems WHERE movieId = :movieId AND listName = :listName")
    abstract suspend fun getApiMovieListItem(movieId: Int, listName: String): PredefinedListItem

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPopularMovies(predefinedListsItems: List<PredefinedListItem>)

    @Query("DELETE FROM predefinedListsItems WHERE listName = :listName")
    abstract suspend fun deleteAllMoviesFromApiList(listName: String)
}