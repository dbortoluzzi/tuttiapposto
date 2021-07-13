package it.dbortoluzzi.tuttiapposto.ui

interface BaseMvpPresenter<in V : BaseMvpView> {

    fun onStartView()

    fun onAttachView()

    fun onDetachView()
}