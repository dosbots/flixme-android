package com.dosbots.flixme.data.repository.paging

import com.dosbots.flixme.data.api.movies.MoviesApi
import com.dosbots.flixme.data.dabase.dao.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.api.PaginatedResponse
import com.dosbots.flixme.data.models.PredefinedMoviesList
import retrofit2.Response
import javax.inject.Inject

class PopularMoviesRemoteMediator @Inject constructor(
    private val moviesApi: MoviesApi,
    moviesDao: MoviesDao
) : PredefinedListsRemoteMediator(moviesDao) {

    override val list: PredefinedMoviesList = PredefinedMoviesList.PopularMovies

    override suspend fun fetchMovies(page: Int): Response<PaginatedResponse<Movie>> {
        return moviesApi.getPopularMovies(page)
    }
}
