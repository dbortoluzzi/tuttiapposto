package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.tuttiapposto.di.App
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.CreateBooking
import it.dbortoluzzi.usecases.GetAvailableTables
import it.dbortoluzzi.usecases.RequestNewLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class LandingBookingPresenter @Inject constructor(
        mView: View?,
        private val getAvailableTables: GetAvailableTables,
        private val createBooking: CreateBooking
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
                    val companyId = "FbF0or0c0NdBphbZcssm"
                    val buildingId = "VTdqvUGCKLWKq0SFkTHx"
                    val roomId = "B29tSJlDqC6J6OG9Jcug"
                    val tableId = "hepxdHgG9Zejol2BWw0F"
                    val userId = "Feyitm6eaoVmCqggutdRjhY2AKB3"

                    val startDate = Date()
                    val endDate = Date()
                    endDate.time = startDate.time + 1000
//                    val companyId = prefs.companyUId!!
//                    val buildingId = selectedAvailabilityFiltersRepository.getBuilding()?.uID
//                    val roomId = selectedAvailabilityFiltersRepository.getRoom()?.uID
//
//                    val startDate = selectedAvailabilityFiltersRepository.getStartDate()?:Date()
//                    val endDate =  selectedAvailabilityFiltersRepository.getEndDate()?:Date(startDate.time + 3600)

                    val bookingCreated = withContext(Dispatchers.IO) { createBooking(Booking(null, userId, companyId, buildingId, roomId, tableId, startDate, endDate)) }

                    if (bookingCreated != null) {
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
