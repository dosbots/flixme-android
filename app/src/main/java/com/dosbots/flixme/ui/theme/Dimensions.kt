package com.dosbots.flixme.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xlg: Dp,
    val xxlg: Dp
)

val LocalDimens = staticCompositionLocalOf<Dimensions> { error("No dimens provided") }

val DefaultDimensions = Dimensions(
    xs = 4.dp,
    sm = 8.dp,
    md = 16.dp,
    lg = 24.dp,
    xlg = 32.dp,
    xxlg = 40.dp
)