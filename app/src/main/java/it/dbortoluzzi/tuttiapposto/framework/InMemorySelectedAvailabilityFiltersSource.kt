package it.dbortoluzzi.tuttiapposto.framework

import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.tuttiapposto.model.Interval
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemorySelectedAvailabilityFiltersSource @Inject constructor(): SelectedAvailabilityFiltersSource {

    private var building: Building? = null
    private var room: Room? = null
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var interval: Interval? = null

    override fun getBuilding(): Building? {
        return building
    }

    override fun getRoom(): Room? {
        return room
    }

    override fun getStartDate(): Date? {
        return startDate
    }

    override fun getEndDate(): Date? {
        return endDate
    }

    override fun getInterval(): Interval? {
        return interval
    }


    override fun setBuilding(building: Building?) {
        this.building = building
    }

    override fun setRoom(room: Room?) {
        this.room = room
    }

    override fun setStartDate(startDate: Date?) {
        this.startDate = startDate
    }

    override fun setEndDate(endDate: Date?) {
        this.endDate = endDate
    }

    override fun setInterval(interval: Interval) {
        this.interval = interval
    }

    override fun clearAllFilters() {
        setBuilding(null)
        setRoom(null)
        setStartDate(null)
        setEndDate(null)
        setInterval(Interval.ALL_DAY)
    }
}

interface SelectedAvailabilityFiltersSource {
    fun getBuilding(): Building?
    fun getRoom(): Room?
    fun getStartDate(): Date?
    fun getEndDate(): Date?
    fun setBuilding(building: Building?)
    fun setRoom(room: Room?)
    fun setStartDate(startDate:Date?)
    fun setEndDate(endDate:Date?)
    fun getInterval(): Interval?
    fun setInterval(interval: Interval)
    fun clearAllFilters()
}
