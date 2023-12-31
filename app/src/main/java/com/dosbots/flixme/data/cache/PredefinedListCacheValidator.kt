package com.dosbots.flixme.data.cache

import com.dosbots.flixme.data.dabase.MoviesDao
import com.dosbots.flixme.data.models.database.PredefinedListItem
import com.dosbots.flixme.data.models.database.PredefinedMoviesList

class PredefinedListCacheValidator(
    private val list: PredefinedMoviesList,
    private val moviesDao: MoviesDao
) : CacheValidator() {

    override suspend fun getLastSyncDate(): Long? {
        return moviesDao.getOldestApiMovieListItem(list.name)?.createdAt
    }

    override suspend fun invalidate() {
        moviesDao.deleteAllMoviesFromApiList(list.name)
    }
}