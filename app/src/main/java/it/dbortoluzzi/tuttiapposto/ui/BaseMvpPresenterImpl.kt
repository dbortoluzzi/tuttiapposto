package it.dbortoluzzi.tuttiapposto.ui

open class BaseMvpPresenterImpl<V : BaseMvpView>(protected var view: V?) : BaseMvpPresenter<V> {

    override fun onAttachView() {

    }

    override fun onDetachView() {
        view = null
    }

}