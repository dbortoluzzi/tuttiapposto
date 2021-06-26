package it.dbortoluzzi.tuttiapposto.model

import android.content.Context
import android.content.SharedPreferences
import it.dbortoluzzi.tuttiapposto.di.prefs

class Prefs (context: Context) {
    private val APP_COMPANY = "COMPANY"

    private val preferences: SharedPreferences = context.getSharedPreferences("tuttiapposto", Context.MODE_PRIVATE)

    var company: String?
        get() = preferences.getString(APP_COMPANY, null)
        set(value) = preferences.edit().putString(APP_COMPANY, value).apply()

}