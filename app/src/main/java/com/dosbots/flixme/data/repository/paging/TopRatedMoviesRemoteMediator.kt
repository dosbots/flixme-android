package com.dosbots.flixme.data.repository.paging

import com.dosbots.flixme.data.api.MoviesApi
import com.dosbots.flixme.data.cache.CacheValidator
import com.dosbots.flixme.data.cache.PredefinedListCacheValidator
import com.dosbots.flixme.data.dabase.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.api.PaginatedResponse
import com.dosbots.flixme.data.models.database.PredefinedMoviesList
import retrofit2.Response
import javax.inject.Inject

class TopRatedMoviesRemoteMediator @Inject constructor(
    private val moviesApi: MoviesApi,
    moviesDao: MoviesDao
) : PredefinedListsRemoteMediator(moviesDao) {

    override val list: PredefinedMoviesList = PredefinedMoviesList.TopRatedMovies

    override suspend fun fetchMovies(page: Int): Response<PaginatedResponse<Movie>> {
        return moviesApi.getTopRatedMovies(page)
    }
}