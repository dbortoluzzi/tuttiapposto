package it.dbortoluzzi.tuttiapposto.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import it.dbortoluzzi.tuttiapposto.model.Prefs
import it.dbortoluzzi.tuttiapposto.ui.util.Constants
import it.dbortoluzzi.tuttiapposto.utils.NotificationUtils

val prefs: Prefs by lazy {
    BaseApp.prefs!!
}

open class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        instance = this

        NotificationUtils().sendAtTimeNotification(Constants.REMINDER_BOOKING_HOUR, Constants.REMINDER_BOOKING_MINUTE,true, this)
    }

    companion object {
        var prefs: Prefs? = null
        var instance: Application? = null
    }
}

@HiltAndroidApp
class App : BaseApp() {

    companion object {
        fun isNetworkConnected(): Boolean {
            var result = false
            val connectivityManager =
                    instance?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
            return result
        }
    }
}