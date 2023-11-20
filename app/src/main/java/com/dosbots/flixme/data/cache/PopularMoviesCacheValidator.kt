package com.dosbots.flixme.data.cache

import com.dosbots.flixme.data.dabase.MoviesDao
import com.dosbots.flixme.data.models.database.ApiMovieListItem
import javax.inject.Inject

class PopularMoviesCacheValidator @Inject constructor(
    private val moviesDao: MoviesDao
) : CacheValidator() {

    override suspend fun getLastSyncDate(): Long? {
        return moviesDao.getOldestApiMovieListItem(ApiMovieListItem.POPULAR_MOVIES_LIST)?.createdAt
    }

    override suspend fun invalidate() {
        moviesDao.deleteAllMoviesFromApiList(ApiMovieListItem.POPULAR_MOVIES_LIST)
    }
}