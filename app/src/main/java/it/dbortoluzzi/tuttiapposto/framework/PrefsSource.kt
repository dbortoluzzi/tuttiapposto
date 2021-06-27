package it.dbortoluzzi.tuttiapposto.framework

import it.dbortoluzzi.tuttiapposto.di.BaseApp
import it.dbortoluzzi.tuttiapposto.model.Prefs

class PrefsSource {

    val prefs: Prefs by lazy {
        BaseApp.prefs!!
    }

}