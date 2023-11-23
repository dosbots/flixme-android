package com.dosbots.flixme.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dosbots.flixme.data.dabase.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.database.PredefinedMoviesList
import com.dosbots.flixme.data.repository.paging.PopularMoviesRemoteMediator
import com.dosbots.flixme.data.repository.paging.TopRatedMoviesRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MoviesRepository {
    fun getPopularMovies(): Flow<PagingData<Movie>>
    fun getTopRatedMovies(): Flow<PagingData<Movie>>
}

@OptIn(ExperimentalPagingApi::class)
class MoviesRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val popularMoviesRemoteMediator: PopularMoviesRemoteMediator,
    private val topRatedMoviesRemoteMediator: TopRatedMoviesRemoteMediator
) : MoviesRepository {

    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = MOVIES_PAGE_SIZE,
                prefetchDistance = MOVIES_PREFETCH_DISTANCE
            ),
            remoteMediator = popularMoviesRemoteMediator
        ) {
            moviesDao.getMoviesInPredefinedListPaginated(PredefinedMoviesList.PopularMovies.name)
        }.flow
    }

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = MOVIES_PAGE_SIZE,
                prefetchDistance = MOVIES_PREFETCH_DISTANCE
            ),
            remoteMediator = topRatedMoviesRemoteMediator
        ) {
            moviesDao.getMoviesInPredefinedListPaginated(PredefinedMoviesList.TopRatedMovies.name)
        }.flow
    }
}

private const val MOVIES_PAGE_SIZE = 20
private const val MOVIES_PREFETCH_DISTANCE = 10