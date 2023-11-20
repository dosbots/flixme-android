package com.dosbots.flixme.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.ui.theme.FlixmeUi

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val items = viewModel.state.collectAsLazyPagingItems()
    HomeScreen(state = items)
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    state: LazyPagingItems<Movie>
) {
    Column(modifier = modifier) {
        Text(
            text = "Home screen",
            style = FlixmeUi.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
        Text(
            text = "Popular movies",
            style = FlixmeUi.typography.titleSmall
        )
        LazyColumn(modifier) {
            items(state.itemCount) { index ->
                Text(
                    text = state[index]?.title + " index: $index" ?: "null title",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}