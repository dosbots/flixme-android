package com.dosbots.flixme.data.dabase

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.database.PopularMovie

@Dao
abstract class MoviesDao {

    @Query("SELECT * FROM movies WHERE id = :id")
    abstract suspend fun getMovieById(id: Int): Movie

    @Query("SELECT * FROM movies m JOIN popularMovies pm ON m.id = pm.movieId ORDER BY pm.page")
    abstract fun getPopularMovies(): PagingSource<Int, Movie>

    @Query("SELECT * FROM popularMovies WHERE movieId = :movieId")
    abstract suspend fun getPopularMovie(movieId: Int): PopularMovie

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertMovies(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPopularMovies(popularMovies: List<PopularMovie>)

    @Query("DELETE FROM popularMovies")
    abstract suspend fun deleteAllPopularMovies()

    @Transaction
    suspend fun savePopularMovies(movies: List<Movie>, popularMovies: List<PopularMovie>) {
        insertMovies(movies)
        insertPopularMovies(popularMovies)
    }
}