package com.dosbots.flixme.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dosbots.flixme.data.api.MoviesApi
import com.dosbots.flixme.data.dabase.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.database.PopularMovie
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator @Inject constructor(
    private val moviesDao: MoviesDao,
    private val moviesApi: MoviesApi
) : RemoteMediator<Int, Movie>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val loadKey: Int = when (loadType) {
            LoadType.REFRESH -> 0 // first page
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val lastItem: Movie = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)

                val popularMovie = moviesDao.getPopularMovie(lastItem.id)
                popularMovie.page
            }
        }
        return try {
            val moviesResponse = moviesApi.getPopularMovies(loadKey)
            if (moviesResponse.isSuccessful) {
                val paginatedResponse = moviesResponse.body()!!

                val movies = paginatedResponse.results
                val popularMovies = movies.map {
                    PopularMovie(movieId = it.id, page = paginatedResponse.page)
                }
                moviesDao.savePopularMovies(movies = movies, popularMovies = popularMovies)

                MediatorResult.Success(
                    endOfPaginationReached = paginatedResponse.page == paginatedResponse.totalPages
                )
            } else {
                MediatorResult.Error(IOException(moviesResponse.errorBody()?.string()))
            }
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }
}