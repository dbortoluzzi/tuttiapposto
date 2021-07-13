package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.framework.SelectedAvailabilityFiltersRepository
import it.dbortoluzzi.tuttiapposto.model.Interval
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.GetBuildings
import it.dbortoluzzi.usecases.GetRooms
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class FilterAvailabilitiesPresenter @Inject constructor(
        mView: View?,
        private val selectedAvailabilityFiltersRepository: SelectedAvailabilityFiltersRepository,
        private val getBuildings: GetBuildings,
        private val getRooms: GetRooms
) : BaseMvpPresenterImpl<FilterAvailabilitiesPresenter.View>(mView){

    interface View : BaseMvpView {
        fun goToAvailabilitiesPage()
        fun getBuildingSelected(): Pair<String, String>?
        fun getRoomSelected(): Pair<String, String>?
        fun getDateSelected(): Date
        fun getIntervalSelected(): String?
        fun renderBuildings(buildings: List<Building>)
        fun renderRooms(rooms: List<Room>)
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

    companion object {
        private val TAG = "FilterAvailabilitiesPresenter"
    }
}
