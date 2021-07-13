package it.dbortoluzzi.tuttiapposto.framework

import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Room
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedAvailabilityFiltersRepository @Inject constructor(
        private val selectedAvailabilityFiltersSource: SelectedAvailabilityFiltersSource
) {

    fun getBuilding(): Building? {
        return selectedAvailabilityFiltersSource.getBuilding()
    }

    fun getRoom(): Room? {
        return selectedAvailabilityFiltersSource.getRoom()
    }

    fun getStartDate(): Date? {
        return selectedAvailabilityFiltersSource.getStartDate()
    }

    fun getEndDate(): Date? {
        return selectedAvailabilityFiltersSource.getEndDate()
    }

    fun setBuilding(building: Building?) {
        return selectedAvailabilityFiltersSource.setBuilding(building)
    }

    fun setRoom(room: Room?) {
        return selectedAvailabilityFiltersSource.setRoom(room)
    }

    fun setStartDate(startDate: Date) {
        return selectedAvailabilityFiltersSource.setStartDate(startDate)
    }

    fun setEndDate(endDate: Date) {
        return selectedAvailabilityFiltersSource.setEndDate(endDate)
    }
}
