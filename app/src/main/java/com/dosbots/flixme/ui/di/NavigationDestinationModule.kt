package com.dosbots.flixme.ui.di

import com.dosbots.flixme.ui.navigation.NavigationDestination
import com.dosbots.flixme.ui.screens.home.HomeScreenDestination
import com.dosbots.flixme.ui.screens.userdisabled.UserDisabledDestination
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
abstract class NavigationDestinationModule {

    @IntoSet
    @Binds
    abstract fun bindDeveloperLoginDestination(
        destination: UserDisabledDestination
    ): NavigationDestination

    @IntoSet
    @Binds
    abstract fun bindHomeScreenDestination(
        destination: HomeScreenDestination
    ): NavigationDestination
}
