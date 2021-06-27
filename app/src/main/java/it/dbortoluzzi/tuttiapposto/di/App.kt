package it.dbortoluzzi.tuttiapposto.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import it.dbortoluzzi.tuttiapposto.model.Prefs

val prefs: Prefs by lazy {
    BaseApp.prefs!!
}

open class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }

    companion object {
        var prefs: Prefs? = null
    }
}

@HiltAndroidApp
class App : BaseApp()