package com.example.weatherprojecttry_1.data.network.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.getSystemService
import com.example.weatherprojecttry_1.utils.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An interceptor that intercepts the current request
 * and checks if connection exists
 * if there is no connection, it throws NoConnectionException
 * else it proceeds the current request.
 */
class ConnectivityInterceptor(context: Context): Interceptor {
    private val appContext: Context = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()) {
            throw NoConnectionException()
        } else {
            return chain.proceed(chain.request())
        }
    }

    /**
     * Helper method that checks whether the device is connected or not
     * It uses different methods to check connectivity status of different APIs
     */
    private fun isOnline(): Boolean {
        val manager: ConnectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager.activeNetwork ?: return false
            val networkCapabilities = manager.getNetworkCapabilities(network) ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val activeNetworkInfo = manager.activeNetworkInfo ?: return false
            return activeNetworkInfo.isConnected
        }
    }

}