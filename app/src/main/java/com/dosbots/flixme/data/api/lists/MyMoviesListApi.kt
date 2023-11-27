package com.dosbots.flixme.data.api.lists

import com.dosbots.flixme.data.models.MyMoviesList
import com.dosbots.flixme.data.models.MyMoviesListItem

interface MyMoviesListApi {
    suspend fun createNewList(list: MyMoviesList, items: List<MyMoviesListItem>)
    suspend fun insertItemsInList(listId: String, items: List<MyMoviesListItem>)
    suspend fun insertItemInList(listId: String, item: MyMoviesListItem)
}