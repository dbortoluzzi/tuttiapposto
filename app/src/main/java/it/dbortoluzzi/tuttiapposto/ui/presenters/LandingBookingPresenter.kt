package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.di.App
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.CreateBooking
import it.dbortoluzzi.usecases.GetUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class LandingBookingPresenter @Inject constructor(
        mView: View?,
        private val getUser: GetUser,
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

    fun searchAvailabilityToConfirm(companyId: String, buildingId: String, roomId: String, tableId: String, startDate: Date, endDate: Date) {
        if (PrefsValidator.isConfigured(prefs)) {
            if (App.isNetworkConnected()) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showProgressBar();

                    val u = getUser()
                    val user = when (u) {
                        is ServiceResult.Success -> u.data
                        else -> throw Exception("no user found")
                    }

                    val bookingCreated = withContext(Dispatchers.IO) { createBooking(Booking(null, user.uID, companyId, buildingId, roomId, tableId, startDate, endDate)) }

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
