package com.example.weatherprojecttry_1.ui.activities.main

import android.content.Context
import android.provider.SearchRecentSuggestions
import com.example.weatherprojecttry_1.data.RecentQueryProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    @Provides
    fun provideSuggestions(@ActivityContext context: Context): SearchRecentSuggestions =
        SearchRecentSuggestions(context, RecentQueryProvider.AUTHORITY, RecentQueryProvider.MODE)


}