package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.di.App
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.CreateBooking
import it.dbortoluzzi.usecases.GetAvailableTables
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
        private val getAvailableTables: GetAvailableTables,
        private val createBooking: CreateBooking
) : BaseMvpPresenterImpl<LandingBookingPresenter.View>(mView){

    lateinit var companyIdModel: String
    lateinit var buildingIdModel: String
    lateinit var roomIdModel: String
    lateinit var tableIdModel: String
    lateinit var startDateModel: Date
    lateinit var endDateModel: Date

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
                    view?.showProgressBar()

                    val availabilities = withContext(Dispatchers.IO) { getAvailableTables(companyId, buildingId, roomId, startDate, endDate) }

                    if (availabilities.any { it.table.uID == tableId }) {
                        companyIdModel = companyId
                        buildingIdModel = buildingId
                        roomIdModel = roomId
                        tableIdModel = tableId
                        startDateModel = startDate
                        endDateModel = endDate
                        view?.showBookingConfirm()
                    } else {
                        view?.showBookingNotAvailable()
                    }
                    view?.hideProgressBar()
                }
            } else {
                view?.showNetworkError()
            }
        }
    }

    fun bookTable() {
        if (PrefsValidator.isConfigured(prefs)) {
            if (App.isNetworkConnected()) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showProgressBar()

                    val user = when (val u = getUser()) {
                        is ServiceResult.Success -> u.data
                        else -> throw Exception("no user found")
                    }

                    val bookingCreated = withContext(Dispatchers.IO) { createBooking(Booking(null, user.uID, companyIdModel, buildingIdModel, roomIdModel, tableIdModel, startDateModel, endDateModel)) }
                    if (bookingCreated != null) {
                        view?.bookingDoneWithSuccess()
                    } else {
                        view?.bookingNotDoneWithError()
                    }
                    view?.hideProgressBar()
                }
            }
        }
    }

    companion object {
        private val TAG = "LandingBookingPresenter"
    }
}
