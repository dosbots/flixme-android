package com.dosbots.flixme.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dosbots.flixme.data.cache.CacheValidator
import com.dosbots.flixme.data.cache.PredefinedListCacheValidator
import com.dosbots.flixme.data.dabase.MoviesDao
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.api.PaginatedResponse
import com.dosbots.flixme.data.models.database.PredefinedListItem
import com.dosbots.flixme.data.models.database.PredefinedMoviesList
import retrofit2.Response
import java.io.IOException

private const val POPULAR_MOVIES_FIRST_PAGE = 1

@OptIn(ExperimentalPagingApi::class)
abstract class PredefinedListsRemoteMediator(
    private val moviesDao: MoviesDao
) : RemoteMediator<Int, Movie>() {

    protected abstract val list: PredefinedMoviesList

    private val cacheValidator: CacheValidator by lazy { PredefinedListCacheValidator(list, moviesDao) }

    abstract suspend fun fetchMovies(page: Int): Response<PaginatedResponse<Movie>>

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
                    listName = list.name
                )
                popularMovie.page.plus(1)
            }
        }
        return try {
            val moviesResponse = fetchMovies(loadKey)
            if (moviesResponse.isSuccessful) {
                val paginatedResponse = moviesResponse.body()!!

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