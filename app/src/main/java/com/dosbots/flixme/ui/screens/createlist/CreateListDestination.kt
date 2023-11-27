package com.dosbots.flixme.ui.screens.createlist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.dosbots.flixme.ui.navigation.NavigationDestination
import javax.inject.Inject

class CreateListDestination @Inject constructor() : NavigationDestination() {

    override val route: String = ROUTE

    override fun registerDestination(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.composable(ROUTE) {
            CreateListScreen(
                navigateBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }

    companion object {
        const val ROUTE = "create-list"
    }
}