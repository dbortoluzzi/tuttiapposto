package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
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
import it.dbortoluzzi.usecases.GetAvailableTables
import it.dbortoluzzi.usecases.GetRoomsByCompany
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class AvailabilityPresenter @Inject constructor(
        mView: View?,
        private val getAvailableTables: GetAvailableTables,
        private val getRoomsByCompany: GetRoomsByCompany,
        private val selectedAvailabilityFiltersRepository: SelectedAvailabilityFiltersRepository
) : BaseMvpPresenterImpl<AvailabilityPresenter.View>(mView){

    interface View : BaseMvpView {
        fun renderAvailableTables(availabilities: List<Availability>)
        fun showProgressBar()
        fun hideProgressBar()
        fun showNetworkError()
    }

    override fun onAttachView() {
    }

    override fun onStartView() {
        super.onStartView()
        if (PrefsValidator.isConfigured(prefs)) {
            if (App.isNetworkConnected()) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showProgressBar()

                    val companyId = prefs.companyUId!!
                    val buildingId = selectedAvailabilityFiltersRepository.getBuilding()?.uID
                    val roomId = selectedAvailabilityFiltersRepository.getRoom()?.uID

                    val intervalSelected = FilterAvailabilitiesUtil.extractInterval(selectedAvailabilityFiltersRepository.getInterval()?.name)
                    val (startCalendar, endCalendar) = if (selectedAvailabilityFiltersRepository.getStartDate() != null) {
                        val start = Calendar.getInstance().apply { time = selectedAvailabilityFiltersRepository.getStartDate()!!}
                        val end = Calendar.getInstance().apply { time = selectedAvailabilityFiltersRepository.getEndDate()!!}
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
                        avail.toPresentationModel(room?.name?:"Unknown")}
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
                Constants.ROOM_ID to  avail.tableAvailabilityResponseDto.table.roomId,
                Constants.TABLE_ID to  avail.tableAvailabilityResponseDto.table.uID
        )
    }

    companion object {
        private val TAG = "AvailabilityPresenter"
    }
}
