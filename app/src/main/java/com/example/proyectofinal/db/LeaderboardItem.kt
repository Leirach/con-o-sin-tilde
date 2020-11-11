package com.example.proyectofinal.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.pow

@Entity(tableName = "leaderboard")
class LeaderboardItem(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var juego: Int,
    var fecha: Long,
    var aciertos: Int,
    var tiempo: Int,
    var score: Int) {

    constructor(juego: Int, fecha: Long, aciertos: Int, tiempo: Int):
            this(0, juego, fecha, aciertos, tiempo, calculateScore(aciertos, tiempo))

    companion object {
        fun calculateScore(aciertos: Int, tiempo: Int): Int {
            return ((aciertos * 10.0f).pow(2) / tiempo).toInt()
        }
    }
}
