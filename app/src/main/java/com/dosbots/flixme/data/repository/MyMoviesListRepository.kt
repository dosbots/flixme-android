package com.dosbots.flixme.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dosbots.flixme.data.api.lists.MyMoviesListApi
import com.dosbots.flixme.data.dabase.dao.MyMoviesListsDao
import com.dosbots.flixme.data.models.MyMoviesList
import com.dosbots.flixme.data.models.MyMoviesListItem
import com.dosbots.flixme.data.models.MyMoviesListWithMovies
import com.dosbots.flixme.data.models.MyMoviesListWithMoviesItem
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MyMoviesListsRepository {
    suspend fun countMyMoviesList(): Int
    fun getMyMoviesList(): Flow<PagingData<MyMoviesListWithMovies>>
    suspend fun getMostRecentlyEditedLists(): List<MyMoviesListWithMovies>
    suspend fun getMyMoviesListItems(listId: String): Flow<PagingData<MyMoviesListWithMoviesItem>>
    suspend fun addItemsToList(listId: String, items: List<MyMoviesListItem>)
    suspend fun addItemToList(listId: String, item: MyMoviesListItem)
    suspend fun createListWithMovies(list: MyMoviesList, items: List<MyMoviesListItem>)
}

class MyMoviesListsRepositoryImpl @Inject constructor(
    private val myMoviesListsDao: MyMoviesListsDao,
    private val myMoviesListApi: MyMoviesListApi
) : MyMoviesListsRepository {

    override suspend fun countMyMoviesList(): Int {
        return myMoviesListsDao.myMoviesListsCount()
    }

    override fun getMyMoviesList(): Flow<PagingData<MyMoviesListWithMovies>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                prefetchDistance = 10
            ),
            pagingSourceFactory = {
                myMoviesListsDao.getMyMoviesList()
            }
        ).flow
    }

    override suspend fun getMostRecentlyEditedLists(): List<MyMoviesListWithMovies> {
        return myMoviesListsDao.getMostRecentlyEditedMyMoviesList()
    }

    override suspend fun getMyMoviesListItems(listId: String): Flow<PagingData<MyMoviesListWithMoviesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 15
            ),
            pagingSourceFactory = {
                myMoviesListsDao.getMyMoviesListItems(listId = listId)
            }
        ).flow
    }

    override suspend fun addItemsToList(listId: String, items: List<MyMoviesListItem>) {
        myMoviesListsDao.insertMoviesInList(items)
        withContext(NonCancellable) {
            myMoviesListApi.insertItemsInList(listId, items)
            myMoviesListsDao.markMovieItemsAsSynced(
                listId = listId,
                movieIds = items.map { it.movieId }
            )
        }
    }

    override suspend fun addItemToList(listId: String, item: MyMoviesListItem) {
        myMoviesListsDao.insertMovieInList(item)
        withContext(NonCancellable) {
            myMoviesListApi.insertItemInList(listId, item)
            myMoviesListsDao.markMovieItemAsSynced(listId, item.movieId)
        }
    }

    override suspend fun createListWithMovies(list: MyMoviesList, items: List<MyMoviesListItem>) {
        myMoviesListsDao.createListWithItems(list, items)
        withContext(NonCancellable) {
            myMoviesListApi.createNewList(list, items)
            myMoviesListsDao.markMoviesListAsSynced(list.id)
            myMoviesListsDao.markMovieItemsAsSynced(
                listId = list.id,
                movieIds = items.map { it.movieId }
            )
        }
    }
}
