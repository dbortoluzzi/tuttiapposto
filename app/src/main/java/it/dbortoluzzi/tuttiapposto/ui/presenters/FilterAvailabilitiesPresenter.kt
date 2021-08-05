package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.framework.SelectedAvailabilityFiltersRepository
import it.dbortoluzzi.tuttiapposto.model.Interval
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.BUILDING_ID
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.END_DATE
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.ROOM_ID
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.START_DATE
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.TABLE_ID
import it.dbortoluzzi.usecases.GetBuildings
import it.dbortoluzzi.usecases.GetRooms
import it.dbortoluzzi.usecases.GetTablesWithFilters
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class FilterAvailabilitiesPresenter @Inject constructor(
        mView: View?,
        private val selectedAvailabilityFiltersRepository: SelectedAvailabilityFiltersRepository,
        private val getBuildings: GetBuildings,
        private val getRooms: GetRooms,
        private val getTablesWithFilters: GetTablesWithFilters
) : BaseMvpPresenterImpl<FilterAvailabilitiesPresenter.View>(mView){

    interface View : BaseMvpView {
        fun goToAvailabilitiesPage()
        fun getBuildingSelected(): Pair<String, String>?
        fun getRoomSelected(): Pair<String, String>?
        fun getTableSelected(): Pair<String, String>?
        fun getDateSelected(): Date
        fun getIntervalSelected(): String?
        fun renderBuildings(buildings: List<Building>)
        fun renderRooms(rooms: List<Room>)
        fun renderTables(tables: List<Table>)
        fun renderIntervals(intervals: List<Interval>)
    }

    override fun onAttachView() {
        if (PrefsValidator.isConfigured(prefs)) {
            GlobalScope.launch(Dispatchers.Main) {
                val buildings = withContext(Dispatchers.IO) { getBuildings(prefs.companyUId!!) }
                view?.renderBuildings(buildings)
                view?.renderRooms(listOf())
            }
            GlobalScope.launch(Dispatchers.Main) {
                val intervals  = withContext(Dispatchers.IO) { Interval.values().toList() }
                view?.renderIntervals(intervals)
            }
        }
    }

    fun retrieveRooms(buildingId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val rooms = withContext(Dispatchers.IO) { getRooms(prefs.companyUId!!, buildingId) }
            view?.renderRooms(rooms)
        }
    }

    fun retrieveTables(buildingId: String, roomId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val tables = withContext(Dispatchers.IO) { getTablesWithFilters(prefs.companyUId!!, buildingId, roomId) }
            view?.renderTables(tables)
        }
    }

    fun getBuildingSelected() : Building? {
        return selectedAvailabilityFiltersRepository.getBuilding()
    }

    fun getRoomSelected() : Room? {
        return selectedAvailabilityFiltersRepository.getRoom()
    }

    fun getStartDateSelected() : Date? {
        return selectedAvailabilityFiltersRepository.getStartDate()
    }

    fun getEndDate() : Date? {
        return selectedAvailabilityFiltersRepository.getEndDate()
    }

    fun getInterval() : Interval? {
        return selectedAvailabilityFiltersRepository.getInterval()
    }

    fun filterBtnClicked() {
        val intervalSelected = Interval.valueOf(view?.getIntervalSelected()?:Interval.ALL_DAY.name)
        selectedAvailabilityFiltersRepository.setInterval(intervalSelected)

        val (startCalendar, endCalendar) = getStartEndCalendarFromView(intervalSelected)

        val buildingSelected = view?.getBuildingSelected()
        val roomSelected = view?.getRoomSelected()

        selectedAvailabilityFiltersRepository.setStartDate(startCalendar.time)
        selectedAvailabilityFiltersRepository.setEndDate(endCalendar.time)
        if (buildingSelected != null) {
            selectedAvailabilityFiltersRepository.setBuilding(Building(buildingSelected.first, prefs.companyUId!!, true, buildingSelected.second))
            if (roomSelected != null) {
                selectedAvailabilityFiltersRepository.setRoom(Room(roomSelected.first, prefs.companyUId!!, buildingSelected.first, true, roomSelected.second, null))
            } else {
                selectedAvailabilityFiltersRepository.setRoom(null)
            }
        } else {
            selectedAvailabilityFiltersRepository.setBuilding(null)
            selectedAvailabilityFiltersRepository.setRoom(null)
        }

        view?.goToAvailabilitiesPage()
    }

    fun newBookingBtnClicked(): Map<String, Any> {
        val intervalSelected = Interval.valueOf(view?.getIntervalSelected()?:Interval.ALL_DAY.name)
        val (startCalendar, endCalendar) = getStartEndCalendarFromView(intervalSelected)
        val buildingSelected = view?.getBuildingSelected()
        val roomSelected = view?.getRoomSelected()
        val tableSelected = view?.getTableSelected()
        return mapOf(
                START_DATE to startCalendar.time,
                END_DATE to endCalendar.time,
                BUILDING_ID to buildingSelected!!.first,
                ROOM_ID to roomSelected!!.first,
                TABLE_ID to tableSelected!!.first
        )
    }

    private fun getStartEndCalendarFromView(intervalSelected: Interval): Pair<Calendar, Calendar> {
        val start = view?.getDateSelected()!!
        val startCalendar = Calendar.getInstance()
        startCalendar.time = start
        startCalendar.set(Calendar.HOUR_OF_DAY, intervalSelected.startHour)
        startCalendar.set(Calendar.MINUTE, 0)
        startCalendar.set(Calendar.SECOND, 0)

        val end = Date(start.time) //TODO: read from end
        val endCalendar = Calendar.getInstance()
        endCalendar.time = end
        endCalendar.set(Calendar.HOUR_OF_DAY, intervalSelected.endHour)
        endCalendar.set(Calendar.MINUTE, 0)
        endCalendar.set(Calendar.SECOND, 0)
        return Pair(startCalendar, endCalendar)
    }

    companion object {
        private val TAG = "FilterAvailabilitiesPresenter"
    }
}
