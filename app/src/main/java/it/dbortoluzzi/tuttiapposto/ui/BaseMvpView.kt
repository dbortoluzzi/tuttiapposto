package it.dbortoluzzi.tuttiapposto.ui

import android.content.Context
import androidx.annotation.StringRes

interface BaseMvpView {

    fun context(): Context

    fun showError(error: String?)

    fun showError(@StringRes stringResId: Int)

    fun showMessage(@StringRes srtResId: Int)

    fun showMessage(message: String)

}
