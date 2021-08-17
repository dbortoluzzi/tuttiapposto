package it.dbortoluzzi.tuttiapposto.framework

import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.tuttiapposto.model.Interval
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class SelectedAvailabilityFiltersRepositoryImpl constructor(
        private val selectedAvailabilityFiltersSource: SelectedAvailabilityFiltersSource
): SelectedAvailabilityFiltersRepository {

    override fun getBuilding(): Building? {
        return selectedAvailabilityFiltersSource.getBuilding()
    }

    override fun getRoom(): Room? {
        return selectedAvailabilityFiltersSource.getRoom()
    }

    override fun getStartDate(): Date? {
        return selectedAvailabilityFiltersSource.getStartDate()
    }

    override fun getEndDate(): Date? {
        return selectedAvailabilityFiltersSource.getEndDate()
    }

    override fun getInterval(): Interval? {
        return selectedAvailabilityFiltersSource.getInterval()
    }

    override fun setBuilding(building: Building?) {
        return selectedAvailabilityFiltersSource.setBuilding(building)
    }

    override fun setRoom(room: Room?) {
        return selectedAvailabilityFiltersSource.setRoom(room)
    }

    override fun setStartDate(startDate: Date) {
        return selectedAvailabilityFiltersSource.setStartDate(startDate)
    }

    override fun setEndDate(endDate: Date) {
        return selectedAvailabilityFiltersSource.setEndDate(endDate)
    }

    override fun setInterval(interval: Interval) {
        return selectedAvailabilityFiltersSource.setInterval(interval)
    }

    override fun clearAllFilters() {
        return selectedAvailabilityFiltersSource.clearAllFilters();
    }
}

interface SelectedAvailabilityFiltersRepository {

    fun getBuilding(): Building?

    fun getRoom(): Room?

    fun getStartDate(): Date?

    fun getEndDate(): Date?

    fun getInterval(): Interval?

    fun setBuilding(building: Building?)

    fun setRoom(room: Room?)

    fun setStartDate(startDate: Date)

    fun setEndDate(endDate: Date)

    fun setInterval(interval: Interval)

    fun clearAllFilters()

}