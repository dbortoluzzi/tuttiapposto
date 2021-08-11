package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.AvailabilityReport
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.di.App
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.framework.SelectedAvailabilityFiltersRepository
import it.dbortoluzzi.tuttiapposto.model.Availability
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.model.toPresentationModel
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.tuttiapposto.ui.util.Constants
import it.dbortoluzzi.tuttiapposto.ui.util.FilterAvailabilitiesUtil
import it.dbortoluzzi.usecases.CreateAvailabilityReport
import it.dbortoluzzi.usecases.GetAvailableTables
import it.dbortoluzzi.usecases.GetRoomsByCompany
import it.dbortoluzzi.usecases.GetUser
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class AvailabilityPresenter @Inject constructor(
        mView: View?,
        private val getAvailableTables: GetAvailableTables,
        private val getRoomsByCompany: GetRoomsByCompany,
        private val createAvailabilityReport: CreateAvailabilityReport,
        private val selectedAvailabilityFiltersRepository: SelectedAvailabilityFiltersRepository,
        private val getUser: GetUser,
) : BaseMvpPresenterImpl<AvailabilityPresenter.View>(mView) {

    interface View : BaseMvpView {
        fun renderAvailableTables(availabilities: List<Availability>)
        fun availabilityReportDoneWithSuccess()
        fun availabilityReportNotDoneWithError()
        fun showProgressBar()
        fun hideProgressBar()
        fun showNetworkError()
    }

    override fun onAttachView() {
    }

    override fun onStartView() {
        super.onStartView()
        loadAvailabilities()
    }

    private fun loadAvailabilities() {
        if (PrefsValidator.isConfigured(prefs)) {
            if (App.isNetworkConnected()) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showProgressBar()

                    val companyId = prefs.companyUId!!
                    val buildingId = selectedAvailabilityFiltersRepository.getBuilding()?.uID
                    val roomId = selectedAvailabilityFiltersRepository.getRoom()?.uID

                    val intervalSelected = FilterAvailabilitiesUtil.extractInterval(selectedAvailabilityFiltersRepository.getInterval()?.name)
                    val (startCalendar, endCalendar) = if (selectedAvailabilityFiltersRepository.getStartDate() != null) {
                        val start = Calendar.getInstance().apply { time = selectedAvailabilityFiltersRepository.getStartDate()!! }
                        val end = Calendar.getInstance().apply { time = selectedAvailabilityFiltersRepository.getEndDate()!! }
                        Pair(start, end)
                    } else {
                        FilterAvailabilitiesUtil.extractStartEndPair(intervalSelected, Date())
                    }

                    val jobAvailabilities: Deferred<List<TableAvailabilityResponseDto>> = async { withContext(Dispatchers.IO) { getAvailableTables(companyId, buildingId, roomId, startCalendar.time, endCalendar.time) } }
                    val jobRooms: Deferred<List<Room>> = async { withContext(Dispatchers.IO) { getRoomsByCompany(companyId) } }
                    val availabilities = jobAvailabilities.await()
                    val rooms = jobRooms.await()

                    view?.renderAvailableTables(availabilities.map { avail ->
                        val room = rooms.find { it.uID == avail.table.roomId }
                        avail.toPresentationModel(room?.name ?: "Unknown")
                    }
                    )
                    view?.hideProgressBar()
                }
            } else {
                view?.showNetworkError()
            }
        }
    }

    fun newBookingBtnClicked(avail: Availability): Map<String, Any> {
        val intervalSelected = FilterAvailabilitiesUtil.extractInterval(selectedAvailabilityFiltersRepository.getInterval()?.name)
        // TODO: improve
        val (startCalendar, endCalendar) = FilterAvailabilitiesUtil.extractStartEndPair(intervalSelected, selectedAvailabilityFiltersRepository.getStartDate())

        return mapOf(
                Constants.START_DATE to startCalendar.time,
                Constants.END_DATE to endCalendar.time,
                Constants.BUILDING_ID to avail.tableAvailabilityResponseDto.table.buildingId,
                Constants.ROOM_ID to avail.tableAvailabilityResponseDto.table.roomId,
                Constants.TABLE_ID to avail.tableAvailabilityResponseDto.table.uID
        )
    }

    fun reportAvailabilityBtnClicked(avail: Availability) {
        if (PrefsValidator.isConfigured(prefs)) {
            if (App.isNetworkConnected()) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showProgressBar()

                    val user = when (val u = getUser()) {
                        is ServiceResult.Success -> u.data
                        else -> throw Exception("no user found")
                    }

                    val availabilityReport = withContext(Dispatchers.IO) {
                        createAvailabilityReport(AvailabilityReport(null,
                                user.uID,
                                avail.tableAvailabilityResponseDto.table.companyId,
                                avail.tableAvailabilityResponseDto.table.buildingId,
                                avail.tableAvailabilityResponseDto.table.roomId,
                                avail.tableAvailabilityResponseDto.table.uID,
                                avail.tableAvailabilityResponseDto.startDate!!,
                                avail.tableAvailabilityResponseDto.endDate!!)
                        )
                    }
                    if (availabilityReport != null) {
                        view?.availabilityReportDoneWithSuccess()
                    } else {
                        view?.availabilityReportNotDoneWithError()
                    }

                    view?.hideProgressBar()
                }
            } else {
                view?.showNetworkError()
            }
        }
    }

    companion object {
        private val TAG = "AvailabilityPresenter"
    }
}
