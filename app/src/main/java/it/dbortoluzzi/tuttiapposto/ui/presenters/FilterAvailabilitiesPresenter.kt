package it.dbortoluzzi.tuttiapposto.ui.presenters

import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.framework.SelectedAvailabilityFiltersRepository
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpPresenterImpl
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpView
import it.dbortoluzzi.usecases.GetBuildings
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
) : BaseMvpPresenterImpl<FilterAvailabilitiesPresenter.View>(mView){

    interface View : BaseMvpView {
        fun goToAvailabilitiesPage()
        fun getBuildingSelected(): Pair<String, String>?
        fun getRoomSelected(): Pair<String, String>?
        fun renderBuildings(buildings: List<Building>)
        fun renderRooms(rooms: List<Room>)
    }

    override fun onAttachView() {
        if (PrefsValidator.isConfigured(prefs)) {
            GlobalScope.launch(Dispatchers.Main) {
                val buildings = withContext(Dispatchers.IO) { getBuildings(prefs.companyUId!!) }
                view?.renderBuildings(buildings)
                view?.renderRooms(listOf())
            }
        }
    }

    fun retrieveRooms(buildingId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            // TODO
        }
    }

    fun getBuildingSelected() : Building? {
        return selectedAvailabilityFiltersRepository.getBuilding()
    }

    fun getRoomSelected() : Room? {
        return selectedAvailabilityFiltersRepository.getRoom()
    }

    fun filterBtnClicked() {
        // TODO: add business logic
        val start = Date()
        val end = Date(start.time+1000)

        val buildingSelected = view?.getBuildingSelected()
        val roomSelected = view?.getRoomSelected()

        selectedAvailabilityFiltersRepository.setStartDate(start)
        selectedAvailabilityFiltersRepository.setEndDate(end)
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
