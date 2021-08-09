package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.dto.OccupationByHourResponseDto
import it.dbortoluzzi.domain.dto.OccupationByRoomResponseDto
import it.dbortoluzzi.tuttiapposto.di.App
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.Interval
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.model.Score
import it.dbortoluzzi.tuttiapposto.model.toPresentationModel
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.tuttiapposto.ui.util.FilterAvailabilitiesUtil
import it.dbortoluzzi.usecases.GetOccupationByHour
import it.dbortoluzzi.usecases.GetOccupationByRoom
import it.dbortoluzzi.usecases.GetRooms
import it.dbortoluzzi.usecases.GetRoomsByCompany
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class DashboardPresenter @Inject constructor(
        mView: View?,
        private val getOccupationByHour: GetOccupationByHour,
        private val getOccupationByRoom: GetOccupationByRoom,
        private val getRoomsByCompany: GetRoomsByCompany
) : BaseMvpPresenterImpl<DashboardPresenter.View>(mView) {

    interface View : BaseMvpView {
        fun renderHourOccupationChart(scores: List<Score>)
        fun renderRoomOccupationChart(scores: List<Score>)
        fun showProgressBar()
        fun hideProgressBar()
        fun showNetworkError()
    }

    override fun onAttachView() {
        if (PrefsValidator.isConfigured(prefs)) {
            if (App.isNetworkConnected()) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showProgressBar()

                    val companyId = prefs.companyUId!!
                    val intervalSelected = Interval.ALL_DAY
                    val (startCalendar, endCalendar) = FilterAvailabilitiesUtil.extractStartEndPair(intervalSelected, Date())

                    GlobalScope.launch(Dispatchers.Main) {
                        val occupationByHour = withContext(Dispatchers.IO) { getOccupationByHour(companyId, startCalendar.time, endCalendar.time) }
                        view?.renderHourOccupationChart(occupationByHour.map { it.toPresentationModel() })
                    }

                    val jobOccupationPerRoom: Deferred<List<OccupationByRoomResponseDto>> = async { withContext(Dispatchers.IO) { getOccupationByRoom(companyId, startCalendar.time, endCalendar.time) } }
                    val jobRooms: Deferred<List<Room>> = async { withContext(Dispatchers.IO) { getRoomsByCompany(companyId) } }
                    val occupationByRooms = jobOccupationPerRoom.await()
                    val rooms = jobRooms.await()
                    view?.renderRoomOccupationChart(occupationByRooms.map { occ ->
                        val room = rooms.find { it.uID == occ.elementId }
                        occ.toPresentationModel(room?.name?:"Unknown")}
                    )

                    view?.hideProgressBar()
                }
            }
        }
    }

    companion object {
        private val TAG = "DashboardPresenter"
    }
}
