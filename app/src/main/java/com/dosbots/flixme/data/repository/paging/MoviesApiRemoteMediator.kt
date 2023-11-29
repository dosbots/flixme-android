package com.dosbots.flixme.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dosbots.flixme.data.api.movies.MoviesApi.Companion.MOVIES_API_FIRST_PAGE
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.api.PaginatedResponse
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
abstract class MoviesApiRemoteMediator : RemoteMediator<Int, Movie>() {

    protected abstract suspend fun fetchMovies(page: Int): Response<PaginatedResponse<Movie>>
    protected abstract suspend fun getNextPageToFetch(lastItem: Movie): Int
    protected abstract suspend fun saveResponse(paginatedResponse: PaginatedResponse<Movie>)

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val loadKey: Int = when (loadType) {
            LoadType.REFRESH -> MOVIES_API_FIRST_PAGE
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val lastItem: Movie = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                getNextPageToFetch(lastItem)
            }
        }
        return try {
            val moviesResponse = fetchMovies(loadKey)
            if (moviesResponse.isSuccessful) {
                val paginatedResponse = moviesResponse.body()!!

                saveResponse(paginatedResponse)

                MediatorResult.Success(
                    endOfPaginationReached = paginatedResponse.page == paginatedResponse.totalPages
                )
            } else {
                MediatorResult.Error(
                    IOException(moviesResponse.errorBody()?.string())
                )
            }
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }

}