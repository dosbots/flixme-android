package com.dosbots.flixme.data.api.lists

import com.dosbots.flixme.commons.toMap
import com.dosbots.flixme.data.EntityIdGenerator
import com.dosbots.flixme.data.models.MyMoviesList
import com.dosbots.flixme.data.models.MyMoviesListItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreMyMoviesListApi @Inject constructor() : MyMoviesListApi {

    private val firestore = Firebase.firestore

    override suspend fun createNewList(list: MyMoviesList, items: List<MyMoviesListItem>) {
        firestore.collection("lists")
            .document(list.id)
            .set(list)
            .await()
        insertItemsInList(list.id, items)
    }

    override suspend fun insertItemsInList(listId: String, items: List<MyMoviesListItem>) {
        val itemsMap = items.toMap { EntityIdGenerator.newId() }
        firestore.collection("lists")
            .document(listId)
            .collection("items")
            .add(itemsMap)
            .await()
    }

    override suspend fun insertItemInList(listId: String, item: MyMoviesListItem) {
        firestore.collection("lists")
            .document(listId)
            .collection("items")
            .add(item)
            .await()
    }
}
