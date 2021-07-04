package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.Availability
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilterAvailabilitiesPresenter @Inject constructor(
        mView: View?
) : BaseMvpPresenterImpl<FilterAvailabilitiesPresenter.View>(mView){

    interface View : BaseMvpView {
        fun goToAvailabilitiesPage()
    }

    override fun onAttachView() {
        if (PrefsValidator.isConfigured(prefs)) {
            GlobalScope.launch(Dispatchers.Main) {
                // TODO
            }
        }
    }

    fun filterBtnClicked() {
        // TODO: add business logic
        view?.goToAvailabilitiesPage()
    }

    companion object {
        private val TAG = "SearchAvailabilityPresenter"
    }
}
