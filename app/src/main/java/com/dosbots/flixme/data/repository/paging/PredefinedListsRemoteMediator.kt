package com.dosbots.flixme.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import com.dosbots.flixme.data.cache.CacheValidator
import com.dosbots.flixme.data.cache.PredefinedListCacheValidator
import com.dosbots.flixme.data.dabase.dao.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.api.PaginatedResponse
import com.dosbots.flixme.data.models.PredefinedListItem
import com.dosbots.flixme.data.models.PredefinedMoviesList

abstract class PredefinedListsRemoteMediator(
    private val moviesDao: MoviesDao
) : MoviesApiRemoteMediator() {

    protected abstract val list: PredefinedMoviesList

    private val cacheValidator: CacheValidator by lazy { PredefinedListCacheValidator(list, moviesDao) }

    @ExperimentalPagingApi
    override suspend fun initialize(): InitializeAction {
        val popularMoviesNumber = moviesDao.countMoviesInPredefinedList(listName = list.name)
        return if (popularMoviesNumber == 0) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else if (cacheValidator.validateCache()) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun getNextPageToFetch(lastItem: Movie): Int {
        val popularMovie = moviesDao.getApiMovieListItem(
            movieId = lastItem.id,
            listName = list.name
        )
        return popularMovie.page.plus(1)
    }

    override suspend fun saveResponse(paginatedResponse: PaginatedResponse<Movie>) {
        val movies = paginatedResponse.results
        val predefinedListMovies = movies.map {
            PredefinedListItem(
                movieId = it.id,
                listName = list.name,
                page = paginatedResponse.page,
                createdAt = System.currentTimeMillis()
            )
        }
        moviesDao.saveApiMovieListItem(movies = movies, predefinedListsItems = predefinedListMovies)
    }
}