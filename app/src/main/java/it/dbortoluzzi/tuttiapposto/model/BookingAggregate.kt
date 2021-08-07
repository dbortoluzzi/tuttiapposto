package it.dbortoluzzi.tuttiapposto.model

import it.dbortoluzzi.domain.Booking
import java.text.SimpleDateFormat
import java.util.*

data class BookingAggregate(
        val booking: Booking,
        val companyName: String,
        val roomName: String,
        val tableName: String,
        val dateFirstDescription: String,
        val dateSecondDescription: String,
)

fun Booking.toPresentationModel(companyName: String, roomName: String, tableName: String): BookingAggregate = BookingAggregate(
        this,
        companyName,
        roomName,
        tableName,
        "${startDate?.toPrettifiedDateString()} | ${endDate?.toPrettifiedDateString()}",
        if (sameDayOfTheYear(startDate, endDate)) {
            Interval.values()
                    .find { it.startHour == extractHour(startDate) && it.endHour == extractHour(endDate)}
                    ?.description
                    ?: "${startDate.toPrettifiedTimeString()} | ${endDate?.toPrettifiedTimeString()}"
        } else {
            "${startDate.toPrettifiedTimeString()} | ${endDate?.toPrettifiedTimeString()}"
        }
)

private fun Date.toPrettifiedDateString(): String =
        datePattern.run { format(this@toPrettifiedDateString) }

private fun Date.toPrettifiedTimeString(): String =
        timePattern.run { format(this@toPrettifiedTimeString) }

private val datePattern = SimpleDateFormat("dd/MM", Locale.getDefault())
private val timePattern = SimpleDateFormat("HH:mm", Locale.getDefault())
private val dayOfYearPattern = SimpleDateFormat("ddMMyyyy", Locale.getDefault())

private fun sameDayOfTheYear(date1: Date, date2: Date) : Boolean{
    return dayOfYearPattern.format(date1) == dayOfYearPattern.format(date2)
}

private fun extractHour(date: Date) : Int {
    val cal = Calendar.getInstance()
    cal.time = date
    return cal.get(Calendar.HOUR_OF_DAY)
}
