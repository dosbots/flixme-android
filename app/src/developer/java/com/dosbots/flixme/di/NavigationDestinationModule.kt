package com.dosbots.flixme.di

import com.dosbots.flixme.ui.navigation.NavigationDestination
import com.dosbots.flixme.ui.screens.login.DeveloperLoginDestination
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
        destination: DeveloperLoginDestination
    ): NavigationDestination
}
