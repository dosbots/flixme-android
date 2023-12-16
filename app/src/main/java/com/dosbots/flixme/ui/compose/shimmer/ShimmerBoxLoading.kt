package com.dosbots.flixme.ui.compose.shimmer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.dosbots.flixme.ui.theme.FlixmeUi

@Composable
fun ShimmerBoxLoading(
    modifier: Modifier = Modifier,
    color: Color? = null
) {
    Box(
        modifier = modifier
            .shimmer(color = color)
    )
}