package it.dbortoluzzi.tuttiapposto.utils

import java.util.*

object TuttiAppostoUtils {

    fun resetCal(startCal: Calendar) {
        startCal.time = Date()
        startCal[Calendar.HOUR_OF_DAY] = 0
        startCal[Calendar.MINUTE] = 0
        startCal[Calendar.SECOND] = 0
        startCal[Calendar.MILLISECOND] = 0
    }

    fun extractHour(date: Date) : Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.HOUR_OF_DAY)
    }

    fun now() = Date()

    fun isBetween(dateToCheck: Date, startDate: Date, endDate: Date) : Boolean {
        return (startDate.before(dateToCheck) || startDate == dateToCheck) && (endDate.after(dateToCheck) || endDate == dateToCheck)
    }
}