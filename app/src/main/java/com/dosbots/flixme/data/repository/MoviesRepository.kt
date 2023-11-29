package com.dosbots.flixme.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.dosbots.flixme.data.api.movies.MoviesApi
import com.dosbots.flixme.data.api.movies.MoviesApi.Companion.MOVIES_API_FIRST_PAGE
import com.dosbots.flixme.data.dabase.dao.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.PredefinedMoviesList
import com.dosbots.flixme.data.repository.paging.MoviesApiRemoteMediator
import com.dosbots.flixme.data.repository.paging.PopularMoviesRemoteMediator
import com.dosbots.flixme.data.repository.paging.SearchRemoteMediator
import com.dosbots.flixme.data.repository.paging.TopRatedMoviesRemoteMediator
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

interface MoviesRepository {
    fun getMovieFullImagePath(image: String): String
    fun getPopularMovies(): Flow<PagingData<Movie>>
    fun getTopRatedMovies(): Flow<PagingData<Movie>>
    fun searchMovieByTitle(query: String): Flow<PagingData<Movie>>
    suspend fun searchMovieByTitleTopCoincidences(query: String): List<Movie>
}

@OptIn(ExperimentalPagingApi::class)
class MoviesRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val moviesApi: MoviesApi,
    private val popularMoviesRemoteMediator: PopularMoviesRemoteMediator,
    private val topRatedMoviesRemoteMediator: TopRatedMoviesRemoteMediator,
    private val searchRemoteMediator: SearchRemoteMediator
) : MoviesRepository {

    override fun getMovieFullImagePath(image: String): String {
        return "https://image.tmdb.org/t/p/w300/$image"
    }

    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        return moviesPager(popularMoviesRemoteMediator) {
            moviesDao.getMoviesInPredefinedListPaginated(PredefinedMoviesList.PopularMovies.name)
        }
    }

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return moviesPager(topRatedMoviesRemoteMediator) {
            moviesDao.getMoviesInPredefinedListPaginated(PredefinedMoviesList.TopRatedMovies.name)
        }
    }

    override fun searchMovieByTitle(query: String): Flow<PagingData<Movie>> {
        searchRemoteMediator.setQuery(query)

        val sanitizedQuery = "%$query%"
        return moviesPager(searchRemoteMediator) {
            moviesDao.searchMovieByTitle(sanitizedQuery)
        }
    }

    override suspend fun searchMovieByTitleTopCoincidences(query: String): List<Movie> {
        val response = moviesApi.searchMovie(query, MOVIES_API_FIRST_PAGE)
        if (response.isSuccessful) {
            val movies = response.body()!!.results
            moviesDao.insertMovies(movies)
            return movies
        }
        throw IOException("Error searching movies: ${response.errorBody()?.string()}")
    }

    private fun moviesPager(
        remoteMediator: MoviesApiRemoteMediator,
        pagingSourceFactory: () -> PagingSource<Int, Movie>
    ) = Pager(
        config = PagingConfig(
            pageSize = MOVIES_PAGE_SIZE,
            prefetchDistance = MOVIES_PREFETCH_DISTANCE
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = pagingSourceFactory
    ).flow
}

private const val MOVIES_PAGE_SIZE = 20
private const val MOVIES_PREFETCH_DISTANCE = 10