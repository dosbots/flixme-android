package com.dosbots.flixme.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.dosbots.flixme.R
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    popularMoviesState: LazyPagingItems<HomeScreenMovie>,
    topRatedMoviesState: LazyPagingItems<HomeScreenMovie>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.home_screen_title),
                        style = FlixmeUi.typography.displayMedium
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = FlixmeUi.colorScheme.background
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues) // ignore, not using bottom bar
                .padding(horizontal = FlixmeUi.dimens.md)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
            MoviesScrollableRow(
                title = "Popular movies",
                movies = popularMoviesState
            )
            Spacer(modifier = Modifier.height(FlixmeUi.dimens.lg))
            MoviesScrollableRow(
                title = "Top rated movies",
                movies = topRatedMoviesState
            )
        }
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
                                .width(movieListItemWidth)
                                .height(movieItemLoadingHeight)
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
                .width(movieListItemWidth)
                .padding(FlixmeUi.dimens.md)
        ) {
            MovieImage(
                imageUrl = movie.imageUrl
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

@Composable
private fun MovieImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        var loadingInProgress by remember { mutableStateOf(true) }
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .height(movieImageHeight)
                .fillMaxWidth()
                .clip(FlixmeUi.shapes.large),
            onState = { state ->
                when(state) {
                    is AsyncImagePainter.State.Loading -> {
                        loadingInProgress = true
                    }
                    AsyncImagePainter.State.Empty -> {
                        // no-op
                    }
                    is AsyncImagePainter.State.Success -> {
                        loadingInProgress = false
                    }
                    is AsyncImagePainter.State.Error -> {
                        loadingInProgress = false
                    }
                }
            }
        )
        if (loadingInProgress) {
            ShimmerBoxLoading(
                modifier = Modifier
                    .width(movieListItemWidth)
                    .height(movieImageHeight)
            )
        }
    }
}


private val movieListItemWidth = 200.dp
private val movieImageHeight = 120.dp
private val movieItemLoadingHeight = 148.dp
