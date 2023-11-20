package com.dosbots.flixme.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dosbots.flixme.data.api.MoviesApi
import com.dosbots.flixme.data.cache.PopularMoviesCacheValidator
import com.dosbots.flixme.data.dabase.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.database.ApiMovieListItem
import com.dosbots.flixme.data.models.database.ApiMovieListItem.Companion.POPULAR_MOVIES_LIST
import java.io.IOException
import javax.inject.Inject

private const val POPULAR_MOVIES_FIRST_PAGE = 1

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator @Inject constructor(
    private val moviesDao: MoviesDao,
    private val moviesApi: MoviesApi,
    private val cacheValidator: PopularMoviesCacheValidator
) : RemoteMediator<Int, Movie>() {

    override suspend fun initialize(): InitializeAction {
        val popularMoviesNumber = moviesDao.countPopularMovies()
        return if (popularMoviesNumber == 0) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else if (cacheValidator.validateCache()) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val loadKey: Int = when (loadType) {
            LoadType.REFRESH -> POPULAR_MOVIES_FIRST_PAGE
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val lastItem: Movie = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)

                val popularMovie = moviesDao.getApiMovieListItem(
                    movieId = lastItem.id,
                    listName = POPULAR_MOVIES_LIST
                )
                popularMovie.page.plus(1)
            }
        }
        return try {
            val moviesResponse = moviesApi.getPopularMovies(loadKey)
            if (moviesResponse.isSuccessful) {
                val paginatedResponse = moviesResponse.body()!!

                val movies = paginatedResponse.results
                val popularMovies = movies.map {
                    ApiMovieListItem(
                        movieId = it.id,
                        listName = POPULAR_MOVIES_LIST,
                        page = paginatedResponse.page,
                        createdAt = System.currentTimeMillis()
                    )
                }
                moviesDao.saveApiMovieListItem(movies = movies, apiMovieListItems = popularMovies)

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