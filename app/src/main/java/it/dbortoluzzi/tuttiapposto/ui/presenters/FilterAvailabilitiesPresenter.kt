package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.framework.SelectedAvailabilityFiltersRepository
import it.dbortoluzzi.tuttiapposto.model.Availability
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FilterAvailabilitiesPresenter @Inject constructor(
        mView: View?,
        private val selectedAvailabilityFiltersRepository: SelectedAvailabilityFiltersRepository
) : BaseMvpPresenterImpl<FilterAvailabilitiesPresenter.View>(mView){

    interface View : BaseMvpView {
        fun goToAvailabilitiesPage()
    }

    override fun onAttachView() {
        if (PrefsValidator.isConfigured(prefs)) {
//            GlobalScope.launch(Dispatchers.Main) {
//
//            }
        }
    }

    fun filterBtnClicked() {
        // TODO: add business logic
        val start = Date()
        val end = Date(start.time+1000)
        selectedAvailabilityFiltersRepository.setStartDate(start)
        selectedAvailabilityFiltersRepository.setEndDate(end)
        selectedAvailabilityFiltersRepository.setBuilding(Building("asd", prefs.companyUId!!, true, "CASDAsd"))
        view?.goToAvailabilitiesPage()
    }

    companion object {
        private val TAG = "SearchAvailabilityPresenter"
    }
}
