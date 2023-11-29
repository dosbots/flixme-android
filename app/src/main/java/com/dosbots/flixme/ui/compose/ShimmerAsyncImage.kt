package com.dosbots.flixme.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isUnspecified
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.dosbots.flixme.ui.compose.shimmer.ShimmerBoxLoading
import com.dosbots.flixme.ui.theme.FlixmeUi

@Composable
fun ShimmerAsyncImage(
    imageUrl: String,
    shimmerHeight: Dp,
    shimmerWidth: Dp,
    modifier: Modifier = Modifier,
    imageWidth: Dp = Dp.Unspecified,
    imageHeight: Dp = Dp.Unspecified
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        var loadingInProgress by remember { mutableStateOf(true) }
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .applyIf(imageWidth.isUnspecified) { fillMaxWidth() }
                .applyIf(imageWidth.isUnspecified.not()) { width(imageWidth) }
                .applyIf(imageHeight.isUnspecified) { fillMaxHeight() }
                .applyIf(imageHeight.isUnspecified.not()) { height(imageHeight) }
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
                    .width(shimmerWidth)
                    .height(shimmerHeight)
            )
        }
    }
}

private fun Modifier.applyIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        this then modifier(this)
    } else {
        this
    }
}