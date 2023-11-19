package com.dosbots.flixme.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.dosbots.flixme.ui.navigation.NavigationDestination

@Composable
fun FlixmeApp(
    destinations: Set<NavigationDestination>,
    navHostController: NavHostController
) {
    val initialRoute = destinations.firstOrNull { it.initial }?.route
        ?: error("An initial destination must be provided")
    NavHost(
        navController = navHostController,
        startDestination = initialRoute
    ) {
        destinations.forEach {
            it.registerDestination(this, navHostController)
        }
    }
}