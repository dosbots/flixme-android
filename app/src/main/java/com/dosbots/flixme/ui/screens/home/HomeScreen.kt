package com.dosbots.flixme.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.dosbots.flixme.ui.compose.shimmer.ShimmerBoxLoading
import com.dosbots.flixme.ui.theme.FlixmeUi

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val popularMoviesState = viewModel.popularMoviesState.collectAsLazyPagingItems()
    val topRatedMoviesState = viewModel.topRatedMovies.collectAsLazyPagingItems()
    HomeScreen(
        popularMoviesState = popularMoviesState,
        topRatedMoviesState = topRatedMoviesState,
        modifier = modifier
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    popularMoviesState: LazyPagingItems<HomeScreenMovie>,
    topRatedMoviesState: LazyPagingItems<HomeScreenMovie>
) {
    Column(modifier = modifier) {
        Text(
            text = "Home screen",
            style = FlixmeUi.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
        MoviesScrollableRow(
            title = "Popular movies",
            movies = popularMoviesState
        )
        Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
        MoviesScrollableRow(
            title = "Top rated movies",
            movies = topRatedMoviesState
        )
    }
}

@Composable
private fun MoviesScrollableRow(
    modifier: Modifier = Modifier,
    title: String,
    movies: LazyPagingItems<HomeScreenMovie>
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = FlixmeUi.typography.titleSmall
        )
        Spacer(
            modifier = Modifier.height(FlixmeUi.dimens.sm)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(FlixmeUi.dimens.sm)
        ) {
            items(movies.itemCount) { index ->
                movies[index]?.let {
                    MovieItem(movie = it)
                }
            }
            when {
                movies.loadState.refresh is LoadState.Loading -> {
                    item {
                        ShimmerBoxLoading(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                    }
                }
                movies.loadState.refresh is LoadState.Error -> {

                }
                movies.loadState.append is LoadState.Loading -> {
                    item {
                        ShimmerBoxLoading(
                            modifier = Modifier
                                .width(100.dp)
                                .aspectRatio(3.5f)
                        )
                    }
                }
                movies.loadState.append is LoadState.Error -> {

                }
            }
        }
    }
}

@Composable
private fun MovieItem(
    modifier: Modifier = Modifier,
    movie: HomeScreenMovie
) {
    Card(
        shape = FlixmeUi.shapes.large
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .width(200.dp)
                .padding(FlixmeUi.dimens.md)
        ) {
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,

                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(FlixmeUi.shapes.large)
            )
            Spacer(
                modifier = Modifier.height(FlixmeUi.dimens.sm)
            )
            Text(
                text = movie.title,
                style = FlixmeUi.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
