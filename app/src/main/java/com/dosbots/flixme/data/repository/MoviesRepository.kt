package com.dosbots.flixme.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dosbots.flixme.data.dabase.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.repository.paging.PopularMoviesRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MoviesRepository {
    fun getPopularMovies(): Flow<PagingData<Movie>>
}

class MoviesRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val popularMoviesRemoteMediator: PopularMoviesRemoteMediator
) : MoviesRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = POPULAR_MOVIES_PAGE_SIZE,
                prefetchDistance = POPULAR_MOVIES_PREFETCH_DISTANCE
            ),
            remoteMediator = popularMoviesRemoteMediator
        ) {
            moviesDao.getPopularMoviesPaginated()
        }.flow
    }
}

private const val POPULAR_MOVIES_PAGE_SIZE = 20
private const val POPULAR_MOVIES_PREFETCH_DISTANCE = 10