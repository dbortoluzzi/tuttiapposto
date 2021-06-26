package it.dbortoluzzi.tuttiapposto.ui

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.CustomTestApplication
import it.dbortoluzzi.tuttiapposto.di.BaseApp

@CustomTestApplication(BaseApp::class)
interface HiltTestApplication

class MyCustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        // compilation errors in the IDE here are expected until first build (according to HILT docs)
        return super.newApplication(cl, HiltTestApplication_Application::class.java.name, context)
    }
}