package com.dosbots.flixme.data.dabase.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dosbots.flixme.data.models.MyMoviesList
import com.dosbots.flixme.data.models.MyMoviesListItem
import com.dosbots.flixme.data.models.MyMoviesListWithMovies
import com.dosbots.flixme.data.models.MyMoviesListWithMoviesItem

@Dao
abstract class MyMoviesListsDao {

    @Transaction
    open suspend fun createListWithItems(
        list: MyMoviesList,
        items: List<MyMoviesListItem>
    ) {
        createList(list)
        insertMoviesInList(items)
    }

    @Query("SELECT COUNT(*) FROM MyMoviesList")
    abstract suspend fun myMoviesListsCount(): Int

    @Query("SELECT * FROM MyMoviesList WHERE id = :listId")
    abstract suspend fun getMyList(listId: String): MyMoviesList

    @Query("SELECT * FROM MyMoviesList")
    @Transaction
    abstract fun getMyMoviesList(): PagingSource<Int, MyMoviesListWithMovies>

    @Query("SELECT * FROM MyMoviesList")
    @Transaction
    abstract suspend fun getMostRecentlyEditedMyMoviesList(): List<MyMoviesListWithMovies>

    @Query(
        "SELECT m.title as movieTitle, " +
        "m.backdropPath as moviePoster, " +
        "m.overview as movieDescription, " +
        "items.watched as watched, " +
        "items.addedAt as dateAdded " +
        "FROM MyMoviesListItem items " +
        "JOIN movies m ON items.movieId = m.id " +
        "WHERE items.listId = :listId " +
        "ORDER BY items.addedAt DESC"
    )
    abstract fun getMyMoviesListItems(listId: String): PagingSource<Int, MyMoviesListWithMoviesItem>

    @Insert
    abstract suspend fun createList(list: MyMoviesList)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertMoviesInList(movie: List<MyMoviesListItem>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertMovieInList(movie: MyMoviesListItem)

    @Query("UPDATE MyMoviesList SET synced = 1 WHERE id = :listId")
    abstract suspend fun markMoviesListAsSynced(listId: String)

    @Query("UPDATE MyMoviesListItem SET synced = 1 WHERE listId = :listId AND movieId IN (:movieIds)")
    abstract suspend fun markMovieItemsAsSynced(listId: String, movieIds: List<Int>)

    @Query("UPDATE MyMoviesListItem SET synced = 1 WHERE listId = :listId AND movieId = :movieId")
    abstract suspend fun markMovieItemAsSynced(listId: String, movieId: Int)
}