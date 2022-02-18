package com.example.weatherprojecttry_1.data.network

import android.content.Context
import com.example.weatherprojecttry_1.data.network.interceptors.ConnectivityInterceptor
import com.example.weatherprojecttry_1.data.network.interceptors.KeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named(value = "keyInterceptor")
    fun provideKeyInterceptor(): Interceptor = KeyInterceptor()

    @Provides
    @Named(value = "connectionInterceptor")
    fun provideConnectionInterceptor(@ApplicationContext context: Context): Interceptor =
        ConnectivityInterceptor(context)

    @Provides
    fun provideAPI(
        @Named("keyInterceptor") keyInterceptor: Interceptor,
        @Named("connectionInterceptor") connectivityInterceptor: Interceptor
    ) : WeatherStackAPI = WeatherStackAPI.instantiate(keyInterceptor, connectivityInterceptor)
}