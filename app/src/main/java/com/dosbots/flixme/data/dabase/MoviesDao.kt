package com.dosbots.flixme.data.dabase

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.database.ApiMovieListItem

@Dao
abstract class MoviesDao {

    @Query("SELECT * FROM movies WHERE id = :id")
    abstract suspend fun getMovieById(id: Int): Movie

    @Query("SELECT m.* FROM movies m JOIN apiMovieListItems pm ON m.id = pm.movieId WHERE pm.listName = 'popular' ORDER BY pm.page")
    abstract fun getPopularMoviesPaginated(): PagingSource<Int, Movie>

    @Query("SELECT COUNT(*) FROM apiMovieListItems WHERE listName = 'popular'")
    abstract suspend fun countPopularMovies(): Int

    @Query("SELECT * FROM apiMovieListItems WHERE listName = :listName ORDER BY createdAt LIMIT 1")
    abstract suspend fun getOldestApiMovieListItem(listName: String): ApiMovieListItem?

    @Query("SELECT * FROM apiMovieListItems WHERE movieId = :movieId AND listName = :listName")
    abstract suspend fun getApiMovieListItem(movieId: Int, listName: String): ApiMovieListItem

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertMovies(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPopularMovies(apiMovieListItems: List<ApiMovieListItem>)

    @Query("DELETE FROM apiMovieListItems WHERE listName = :listName")
    abstract suspend fun deleteAllMoviesFromApiList(listName: String)

    @Transaction
    open suspend fun saveApiMovieListItem(movies: List<Movie>, apiMovieListItems: List<ApiMovieListItem>) {
        insertMovies(movies)
        insertPopularMovies(apiMovieListItems)
    }
}