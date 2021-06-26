package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import javax.inject.Inject

class OptionsPresenter @Inject constructor(
        mView: View?
) : BaseMvpPresenterImpl<OptionsPresenter.View>(mView){

    interface View : BaseMvpView {
        fun getCompany(): String
        fun onSuccessSave()
    }

    fun doSaveOptions() {
        // TODO:
        prefs.company = view?.getCompany()
        view?.onSuccessSave()
    }

    companion object {
        private val TAG = "OptionsPresenter"
    }
}
