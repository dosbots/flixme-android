package com.dosbots.flixme.data.repository.paging

import com.dosbots.flixme.data.api.movies.MoviesApi
import com.dosbots.flixme.data.dabase.dao.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.api.PaginatedResponse
import retrofit2.Response
import javax.inject.Inject

class SearchRemoteMediator @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao
) : MoviesApiRemoteMediator() {

    private var query: String = ""
    private var lastPageQueried: Int = 0

    fun setQuery(query: String) {
        this.query = query
    }

    override suspend fun fetchMovies(page: Int): Response<PaginatedResponse<Movie>> {
        lastPageQueried = page
        return moviesApi.searchMovie(query, page)
    }

    override suspend fun getNextPageToFetch(lastItem: Movie): Int {
        return lastPageQueried.plus(1)
    }

    override suspend fun saveResponse(paginatedResponse: PaginatedResponse<Movie>) {
        moviesDao.insertMovies(paginatedResponse.results)
    }
}
