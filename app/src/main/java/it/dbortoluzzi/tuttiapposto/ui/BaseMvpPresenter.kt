package it.dbortoluzzi.tuttiapposto.ui

import android.os.Bundle

interface BaseMvpPresenter<in V : BaseMvpView> {

    fun onAttachView(savedInstanceState: Bundle?)

    fun onDetachView()
}