package com.dosbots.flixme.data.repository.paging

import com.dosbots.flixme.data.api.MoviesApi
import com.dosbots.flixme.data.cache.CacheValidator
import com.dosbots.flixme.data.cache.PopularMoviesCacheValidator
import com.dosbots.flixme.data.dabase.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.api.PaginatedResponse
import com.dosbots.flixme.data.models.database.PredefinedListItem
import retrofit2.Response
import javax.inject.Inject

class PopularMoviesRemoteMediator @Inject constructor(
    private val moviesApi: MoviesApi,
    moviesDao: MoviesDao
) : PredefinedListsRemoteMediator(moviesDao) {

    override val listName: String = PredefinedListItem.POPULAR_MOVIES_LIST
    override val cacheValidator: CacheValidator = PopularMoviesCacheValidator(moviesDao)

    override suspend fun fetchMovies(page: Int): Response<PaginatedResponse<Movie>> {
        return moviesApi.getPopularMovies(page)
    }
}
