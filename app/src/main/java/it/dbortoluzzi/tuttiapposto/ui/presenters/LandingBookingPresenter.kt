package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.tuttiapposto.di.App
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.GetAvailableTables
import it.dbortoluzzi.usecases.RequestNewLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LandingBookingPresenter @Inject constructor(
        mView: View?,
        private val getAvailableTables: GetAvailableTables,
        private val requestNewLocation: RequestNewLocation//TODO: change
) : BaseMvpPresenterImpl<LandingBookingPresenter.View>(mView){

    interface View : BaseMvpView {
        fun showProgressBar()
        fun hideProgressBar()
        fun showNetworkError()
        fun showBookingConfirm()
        fun showBookingNotAvailable()
        fun confirmBookingBtnClicked()
        fun dismissBookingBtnClicked()
        fun bookingDoneWithSuccess()
        fun bookingNotDoneWithError()
    }

    fun searchAvailabilityToConfirm(companyId: String/*, buildingId: String, roomId: String, tableId: String, startDate: Date, endDate: Date*/) {
        if (PrefsValidator.isConfigured(prefs)) {
            if (App.isNetworkConnected()) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showProgressBar();

                    // TODO: read data from intent
//                    val companyId = prefs.companyUId!!
//                    val buildingId = selectedAvailabilityFiltersRepository.getBuilding()?.uID
//                    val roomId = selectedAvailabilityFiltersRepository.getRoom()?.uID
//
//                    val startDate = selectedAvailabilityFiltersRepository.getStartDate()?:Date()
//                    val endDate =  selectedAvailabilityFiltersRepository.getEndDate()?:Date(startDate.time + 3600)

                    // TOOD: do call to availabilities
//                    withContext(Dispatchers.IO) { getAvailableTables(companyId, buildingId, roomId, startDate, endDate) }

                    val isAvailable = true /*TODO:*/
                    if (isAvailable) {
                        view?.showBookingConfirm()
                    } else {
                        view?.showBookingNotAvailable()
                    }
                    view?.hideProgressBar();
                }
            } else {
                view?.showNetworkError()
            }
        }
    }

    fun bookTable() {
        // TODO: do call
        view?.bookingDoneWithSuccess()
    }

    companion object {
        private val TAG = "LandingBookingPresenter"
    }
}
