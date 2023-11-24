package com.dosbots.flixme.ui.screens.home

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.dosbots.flixme.R
import com.dosbots.flixme.ui.compose.shimmer.ShimmerBoxLoading
import com.dosbots.flixme.ui.theme.FlixmeUi
import com.dosbots.flixme.ui.theme.Gray100
import com.dosbots.flixme.ui.utils.LightAndDarkModePreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        popularMoviesState = viewModel.popularMoviesState,
        topRatedMoviesState = viewModel.topRatedMovies,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    popularMoviesState: Flow<PagingData<HomeScreenMovie>>,
    topRatedMoviesState: Flow<PagingData<HomeScreenMovie>>
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
                movies = popularMoviesState.collectAsLazyPagingItems()
            )
            Spacer(modifier = Modifier.height(FlixmeUi.dimens.lg))
            MoviesScrollableRow(
                title = "Top rated movies",
                movies = topRatedMoviesState.collectAsLazyPagingItems()
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
            horizontalArrangement = Arrangement.spacedBy(FlixmeUi.dimens.md)
        ) {
            items(movies.itemCount) { index ->
                movies[index]?.let {
                    MovieItem(movie = it)
                }
            }
            when {
                movies.loadState.refresh is LoadState.Loading -> {
                    items(5) {
                        ShimmerBoxLoading(
                            modifier = Modifier
                                .width(movieListItemWidth)
                                .height(movieItemLoadingHeight)
                        )
                    }
                }
                movies.loadState.refresh is LoadState.Error -> {
                    item {
                        MoviesSectionLoadingError(
                            modifier = Modifier.fillParentMaxWidth()
                        ) {
                            movies.retry()
                        }
                    }
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
                    item {
                        MoviesSectionLoadMoreError {
                            movies.retry()
                        }
                    }
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
        shape = FlixmeUi.shapes.large,
        colors = CardDefaults.cardColors(containerColor = FlixmeUi.colorScheme.surface)
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

@Composable
private fun MoviesSectionLoadingError(
    modifier: Modifier = Modifier,
    onTryAgain: () -> Unit,
) {
    Card(
        shape = FlixmeUi.shapes.large,
        colors = CardDefaults.cardColors(containerColor = FlixmeUi.colorScheme.surface),
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
        Text(
            text = stringResource(id = R.string.generic_error),
            style = FlixmeUi.typography.bodyLarge,
            modifier = Modifier.align(CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
        Button(
            onClick = onTryAgain,
            modifier = Modifier
                .height(ButtonDefaults.MinHeight)
                .align(CenterHorizontally)
        ) {
            Text(
                text = stringResource(
                    id = R.string.try_again
                ),
                style = FlixmeUi.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
    }
}

@Composable
private fun MoviesSectionLoadMoreError(
    modifier: Modifier = Modifier,
    onTryAgain: () -> Unit,
) {
    Card(
        shape = FlixmeUi.shapes.large,
        colors = CardDefaults.cardColors(containerColor = FlixmeUi.colorScheme.surface),
        modifier = modifier
            .width(movieListItemWidth)
            .padding(FlixmeUi.dimens.md)
    ) {
        Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
        Icon(
            painter = painterResource(id = R.drawable.ic_refresh),
            tint = Gray100,
            contentDescription = null,
            modifier = Modifier
                .align(CenterHorizontally)
                .clickable { onTryAgain() }
        )
        Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
    }
}

private val movieListItemWidth = 200.dp
private val movieImageHeight = 120.dp
private val movieItemLoadingHeight = 148.dp

@LightAndDarkModePreview
@Composable
private fun HomeScreenPreview() {
    FlixmeUi {
        Surface {
            val moviesFlow = MutableStateFlow(
                PagingData.from(
                    listOf(
                        HomeScreenMovie(
                            id = 1,
                            title = "Movie 1",
                            imageUrl = ""
                        ),
                        HomeScreenMovie(
                            id = 2,
                            title = "Movie 2",
                            imageUrl = ""
                        ),
                        HomeScreenMovie(
                            id = 3,
                            title = "Movie 2",
                            imageUrl = ""
                        )
                    )
                )
            )

            HomeScreen(
                popularMoviesState = moviesFlow,
                topRatedMoviesState = moviesFlow
            )
        }
    }
}

@LightAndDarkModePreview
@Composable
private fun SectionLoadingError() {
    FlixmeUi {
        Surface {
            MoviesSectionLoadingError(
                onTryAgain = {}
            )
        }
    }
}

@LightAndDarkModePreview
@Composable
private fun LoadMoreMoviesError() {
    FlixmeUi {
        Surface {
            MoviesSectionLoadMoreError(
                onTryAgain = {}
            )
        }
    }
}