package it.dbortoluzzi.tuttiapposto.ui

import android.os.Bundle

open class BaseMvpPresenterImpl<V : BaseMvpView>(protected var view: V?) : BaseMvpPresenter<V> {

    override fun onAttachView(savedInstanceState: Bundle?) {

    }

    override fun onDetachView() {
        view = null
    }

}