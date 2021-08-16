package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.data.R
import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.di.App
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.BookingAggregate
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.model.toPresentationModel
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.tuttiapposto.ui.util.Constants
import it.dbortoluzzi.usecases.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class BookingsPresenter @Inject constructor(
        mView: View?,
        private val getBookings: GetBookings,
        private val getUser: GetUser,
        private val getTables: GetTables,
        private val getCompanies: GetCompanies,
        private val getRoomsByCompany: GetRoomsByCompany,
        private val deleteBooking: DeleteBooking
) : BaseMvpPresenterImpl<BookingsPresenter.View>(mView){

    interface View : BaseMvpView {
        fun renderBookings(bookingAggregates: List<BookingAggregate>)
        fun goToEditBooking(bookingMap: Map<String, Any>)
        fun showProgressBar()
        fun hideProgressBar()
        fun showNetworkError()
    }

    override fun onStartView() {
        super.onStartView()
        loadBookings()
    }

    fun deleteBookingClicked(booking: Booking) {
        GlobalScope.launch(Dispatchers.Main) {
            view?.showProgressBar()
            val deleteResult = withContext(Dispatchers.IO) { deleteBooking(booking) }
            view?.hideProgressBar()
            if (deleteResult) {
                view?.showMessage(R.string.action_delete_success)
            } else {
                view?.showMessage(R.string.action_delete_error)
            }
        }
    }

    fun editBookingClicked(booking: Booking) {
        val bookingMap = mapOf(
                Constants.START_DATE to booking.startDate,
                Constants.END_DATE to booking.endDate,
                Constants.BUILDING_ID to booking.buildingId,
                Constants.ROOM_ID to  booking.roomId,
                Constants.TABLE_ID to  booking.tableId,
                Constants.BOOKING to booking
        )
        view?.goToEditBooking(bookingMap)
    }

    private fun loadBookings() {
        if (PrefsValidator.isConfigured(prefs)) {
            if (App.isNetworkConnected()) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showProgressBar()

                    val companyId = prefs.companyUId!!

                    val user = when (val u = getUser()) {
                        is ServiceResult.Success -> u.data
                        else -> throw Exception("no user found")
                    }

                    val startCal = Calendar.getInstance() // locale-specific

                    val jobBookings: Deferred<List<Booking>> = async { withContext(Dispatchers.IO) { getBookings(user.uID, companyId, null, null, startCal.time, null) } }
                    val jobCompanies: Deferred<List<Company>> = async { withContext(Dispatchers.IO) { getCompanies() } }
                    val jobRooms: Deferred<List<Room>> = async { withContext(Dispatchers.IO) { getRoomsByCompany(companyId) } }
                    val jobTables: Deferred<List<Table>> = async { withContext(Dispatchers.IO) { getTables() } }
                    val companies = jobCompanies.await()
                    val bookings = jobBookings.await()
                    val rooms = jobRooms.await()
                    val tables = jobTables.await()

                    view?.renderBookings(bookings.map { b ->
                        val room = rooms.find { it.uID == b.roomId }
                        val company = companies.find { it.uID == b.companyId }
                        val table = tables.find { it.uID == b.tableId }
                        b.toPresentationModel(view!!.context(), company?.denomination ?: "Unknown company",
                                room?.name ?: "Unknown room", table?.name ?: "Unknown table")
                    }
                    )
                    view?.hideProgressBar()
                }
            } else {
                view?.showNetworkError()
            }
        }
    }

    companion object {
        private val TAG = "BookingPresenter"
    }
}
