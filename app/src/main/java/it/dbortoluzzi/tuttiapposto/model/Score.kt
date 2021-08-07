package it.dbortoluzzi.tuttiapposto.model

data class Score(
        val name:String,
        val score: Int,
        val highlighted: Boolean = false
)