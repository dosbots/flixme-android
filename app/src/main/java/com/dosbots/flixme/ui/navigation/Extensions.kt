package com.dosbots.flixme.ui.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavOptionsBuilder

fun NavOptionsBuilder.popBackStack(navBackStackEntry: NavBackStackEntry) {
    popUpTo(navBackStackEntry.destination.route!!) { inclusive = true }
}