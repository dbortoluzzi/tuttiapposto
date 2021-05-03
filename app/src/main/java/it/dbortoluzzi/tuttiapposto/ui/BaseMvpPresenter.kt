package it.dbortoluzzi.tuttiapposto.ui

interface BaseMvpPresenter<in V : BaseMvpView> {

    fun onAttachView()

    fun onDetachView()
}