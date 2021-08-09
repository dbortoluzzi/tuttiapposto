package it.dbortoluzzi.tuttiapposto.model

import it.dbortoluzzi.domain.dto.OccupationByHourResponseDto
import it.dbortoluzzi.domain.dto.OccupationByRoomResponseDto
import java.text.SimpleDateFormat
import java.util.*

data class Score(
        val name:String,
        val score: Long,
        val highlighted: Boolean = false
)

fun OccupationByHourResponseDto.toPresentationModel(): Score = Score(
        date.toPrettifiedString(),
        occupation,
        highlightedTimeScore(date)
)

fun OccupationByRoomResponseDto.toPresentationModel(room: String): Score = Score(
        room,
        (occupationPercent*100).toLong()
)

private fun Date.toPrettifiedString(): String =
        timePattern.run { format(this@toPrettifiedString) }

private val timePattern = SimpleDateFormat("HH:mm", Locale.getDefault())

fun highlightedTimeScore(date: Date): Boolean{
    val now = Calendar.getInstance()
    val dateCal = Calendar.getInstance()
    dateCal.time = date
    return now.get(Calendar.HOUR_OF_DAY) == dateCal.get(Calendar.HOUR_OF_DAY)
}