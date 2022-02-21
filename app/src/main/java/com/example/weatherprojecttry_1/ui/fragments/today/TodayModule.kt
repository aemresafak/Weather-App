package com.example.weatherprojecttry_1.ui.fragments.today

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(FragmentComponent::class)
object TodayModule {

    @Provides
    fun provideLocationProvider(@ActivityContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    fun provideGeocoder(@ActivityContext context: Context) = Geocoder(context)
}