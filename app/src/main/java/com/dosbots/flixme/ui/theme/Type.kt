package com.dosbots.flixme.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dosbots.flixme.R

val OpenSansFontFamily = FontFamily(
    Font(R.font.open_sans_regular, FontWeight.W400),
    Font(R.font.open_sans_medium, FontWeight.W500),
    Font(R.font.open_sans_regular, FontWeight.W600),
    Font(R.font.open_sans_regular, FontWeight.W700)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 48.sp,
        lineHeight = 54.sp
    ),
    displayMedium = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 32.sp,
        lineHeight = 38.sp
    ),
    displaySmall = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
        lineHeight = 30.sp
    ),
    titleLarge = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),
    titleMedium = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 24.sp,
        lineHeight = 30.sp
    ),
    titleSmall = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 18.sp
    )
)