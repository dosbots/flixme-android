package com.dosbots.flixme.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.dosbots.flixme.ui.theme.FlixmeUi

@Composable
fun ImageCollage(
    imagesUrls: List<String>,
    modifier: Modifier = Modifier
) {
    val collageImages = imagesUrls.take(4)
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (collageImages.size) {
            1 -> {
                ShimmerAsyncImage(
                    imageUrl = imagesUrls[0],
                    modifier = Modifier.clip(FlixmeUi.shapes.large)
                )
            }
            2 -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FlixmeUi.dimens.sm)
                ) {
                    ShimmerAsyncImage(
                        imageUrl = imagesUrls[0],
                        modifier = Modifier
                            .weight(0.5f)
                            .clip(RoundedCornerShape(topStart = FlixmeUi.dimens.md, bottomStart = FlixmeUi.dimens.md))
                    )
                    ShimmerAsyncImage(
                        imageUrl = imagesUrls[1],
                        modifier = Modifier
                            .weight(0.5f)
                            .clip(RoundedCornerShape(topEnd = FlixmeUi.dimens.md, bottomEnd = FlixmeUi.dimens.md))
                    )
                }
            }
            3 -> {
                Row(
                    modifier = Modifier.weight(0.5f)
                ) {
                    ShimmerAsyncImage(
                        imageUrl = imagesUrls[0],
                        modifier = Modifier
                            .weight(0.5f)
                            .clip(RoundedCornerShape(topStart = FlixmeUi.dimens.md))
                    )
                    ShimmerAsyncImage(
                        imageUrl = imagesUrls[1],
                        modifier = Modifier
                            .weight(0.5f)
                            .clip(RoundedCornerShape(topEnd = FlixmeUi.dimens.md))
                    )
                }
                ShimmerAsyncImage(
                    imageUrl = imagesUrls[2],
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = FlixmeUi.dimens.md, bottomEnd = FlixmeUi.dimens.md))
                )
            }
            4 -> {
                Row(
                    modifier = Modifier.weight(0.5f)
                ) {
                    ShimmerAsyncImage(
                        imageUrl = imagesUrls[0],
                        modifier = Modifier
                            .weight(0.5f)
                            .clip(RoundedCornerShape(topStart = FlixmeUi.dimens.md))
                    )
                    ShimmerAsyncImage(
                        imageUrl = imagesUrls[1],
                        modifier = Modifier
                            .weight(0.5f)
                            .clip(RoundedCornerShape(topEnd = FlixmeUi.dimens.md))
                    )
                }
                Row(
                    modifier = Modifier.weight(0.5f)
                ) {
                    ShimmerAsyncImage(
                        imageUrl = imagesUrls[2],
                        modifier = Modifier
                            .weight(0.5f)
                            .clip(RoundedCornerShape(bottomStart = FlixmeUi.dimens.md))
                    )
                    ShimmerAsyncImage(
                        imageUrl = imagesUrls[3],
                        modifier = Modifier
                            .weight(0.5f)
                            .clip(RoundedCornerShape(bottomEnd = FlixmeUi.dimens.md))
                    )
                }
            }
        }
    }
}