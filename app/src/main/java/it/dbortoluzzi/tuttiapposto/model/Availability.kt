package it.dbortoluzzi.tuttiapposto.model

import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import java.text.SimpleDateFormat
import java.util.*

data class Availability(
        val tableAvailabilityResponseDto: TableAvailabilityResponseDto,
        val tableName: String,
        val roomName: String,
        val availabilityNumber: Int,
        val availabilityIntervalDesc: String
)

fun TableAvailabilityResponseDto.toPresentationModel(roomName: String): Availability = Availability(
        this,
        table.name,
        roomName,
        availability,
        "${startDate?.toPrettifiedString()} | ${endDate?.toPrettifiedString()}"
)

private fun Date.toPrettifiedString(): String =
        datePattern.run { format(this@toPrettifiedString) }

private val datePattern = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

enum class Interval(val startHour: Int, val endHour: Int, val description: String) {
    MORNING(9, 13, "Mattino"),
    AFTERNOON(14, 18, "Pomeriggio"),
    ALL_DAY(9, 18, "Tutto il giorno")
}