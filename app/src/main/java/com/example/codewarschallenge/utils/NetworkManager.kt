package com.example.codewarschallenge.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.example.codewarschallenge.R

object NetworkManager {
    fun isOnline(context: Context?): Boolean {
        var result = false
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.run {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                    else -> false
                }
            }
        }
        return result
    }
}

fun Context.reachedTheEnd(): AppResult.Warning{
    return AppResult.Warning(this.resources.getString(R.string.no_more_data))
}

fun Context.noNetworkConnectivityError(): AppResult.Error {
    return AppResult.Error(Exception(this.resources.getString(R.string.no_network_connectivity)))
}

fun Context.apiIsNotResponding(): AppResult.Error{
    return AppResult.Error(Exception(this.resources.getString(R.string.api_not_responding)))
}

fun Context.unknownException(): AppResult.Error{
    return AppResult.Error(Exception(this.resources.getString(R.string.something_went_wrong)))
}