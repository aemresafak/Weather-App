package com.example.weatherprojecttry_1.ui.cities

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(FragmentComponent::class)
object CitiesModule {



    @Provides
    fun getLayoutManager(@ActivityContext context: Context): LinearLayoutManager =
        LinearLayoutManager(context)


}