package it.dbortoluzzi.tuttiapposto.ui.util

import it.dbortoluzzi.tuttiapposto.model.Interval
import java.util.*

object FilterAvailabilitiesUtil {

    fun extractStartEndPair(intervalSelected: Interval, dateSelected: Date?): Pair<Calendar, Calendar> {
        val start = dateSelected?: Date()
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

    fun extractInterval(intervalSelectedStr: String?): Interval {
        return Interval.valueOf(intervalSelectedStr?:Interval.ALL_DAY.name)
    }
}
