package it.dbortoluzzi.tuttiapposto.model

import android.content.Context
import it.dbortoluzzi.data.R
import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.tuttiapposto.utils.TuttiAppostoUtils
import java.text.SimpleDateFormat
import java.util.*

data class BookingAggregate(
        val booking: Booking,
        val companyName: String,
        val roomName: String,
        val tableName: String,
        val current: Boolean,
        val dateFirstDescription: String,
        val dateSecondDescription: String,
)

fun Booking.toPresentationModel(context: Context, companyName: String, roomName: String, tableName: String): BookingAggregate = BookingAggregate(
        this,
        companyName,
        roomName,
        tableName,
        TuttiAppostoUtils.isBetween(TuttiAppostoUtils.now(), startDate, endDate),
        if (sameDayOfTheYear(startDate, endDate)) {
            if (sameDayOfTheYear(startDate, Date())) {
                context.getString(R.string.today)
            } else {
                startDate.toPrettifiedDateString()
            }
        } else {
            "${startDate.toPrettifiedDateString()} - ${endDate.toPrettifiedDateString()}"
        },
        if (sameDayOfTheYear(startDate, endDate)) {
            Interval.values()
                    .find { it.startHour == TuttiAppostoUtils.extractHour(startDate) && it.endHour == TuttiAppostoUtils.extractHour(endDate) }
                    ?.description
                    ?: "${startDate.toPrettifiedTimeString()} - ${endDate.toPrettifiedTimeString()}"
        } else {
            "${startDate.toPrettifiedTimeString()} - ${endDate.toPrettifiedTimeString()}"
        }
)

private fun Date.toPrettifiedDateString(): String =
        datePattern.run { format(this@toPrettifiedDateString) }

private fun Date.toPrettifiedTimeString(): String =
        timePattern.run { format(this@toPrettifiedTimeString) }

private val datePattern = SimpleDateFormat("dd/MM", Locale.getDefault())
private val timePattern = SimpleDateFormat("HH:mm", Locale.getDefault())
private val dayOfYearPattern = SimpleDateFormat("ddMMyyyy", Locale.getDefault())

private fun sameDayOfTheYear(date1: Date, date2: Date): Boolean {
    return dayOfYearPattern.format(date1) == dayOfYearPattern.format(date2)
}




