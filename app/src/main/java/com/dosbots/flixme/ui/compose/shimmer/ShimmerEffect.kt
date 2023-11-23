package com.dosbots.flixme.ui.compose.shimmer

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.dosbots.flixme.ui.theme.Gray100
import com.dosbots.flixme.ui.utils.LightAndDarkModePreview

fun Modifier.shimmer(color: Color? = null): Modifier = composed {
    val shimmerColor = color ?: Gray100
    val shimmerColors = listOf(
        shimmerColor.copy(alpha = 0.6f),
        shimmerColor.copy(alpha = 0.2f),
        shimmerColor.copy(alpha = 0.6f),
    )
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "shimmerEffect")
    val startOffsetX by transition.animateFloat(
        initialValue = -OFFSET_MULTIPLIER * size.width.toFloat(),
        targetValue = OFFSET_MULTIPLIER * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerEffect"
    )
    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned { size = it.size }
}

private const val OFFSET_MULTIPLIER = 1.5f

@LightAndDarkModePreview
@Preview(showBackground = true)
@Composable
private fun ShimmerLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .shimmer()
    )
}