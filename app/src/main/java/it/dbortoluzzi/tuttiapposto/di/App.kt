package it.dbortoluzzi.tuttiapposto.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

open class BaseApp : Application() {
}

@HiltAndroidApp
class App : BaseApp()