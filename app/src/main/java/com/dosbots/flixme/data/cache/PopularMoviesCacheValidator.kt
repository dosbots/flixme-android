package com.dosbots.flixme.data.cache

import com.dosbots.flixme.data.dabase.MoviesDao
import com.dosbots.flixme.data.models.database.PredefinedListItem

class PopularMoviesCacheValidator(
    private val moviesDao: MoviesDao
) : CacheValidator() {

    override suspend fun getLastSyncDate(): Long? {
        return moviesDao.getOldestApiMovieListItem(PredefinedListItem.POPULAR_MOVIES_LIST)?.createdAt
    }

    override suspend fun invalidate() {
        moviesDao.deleteAllMoviesFromApiList(PredefinedListItem.POPULAR_MOVIES_LIST)
    }
}