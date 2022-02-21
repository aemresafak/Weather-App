package com.example.weatherprojecttry_1.ui.fragments.cities.list

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(FragmentComponent::class)
object CityListModule {



    @Provides
    fun getLayoutManager(@ActivityContext context: Context): LinearLayoutManager =
        LinearLayoutManager(context)


}