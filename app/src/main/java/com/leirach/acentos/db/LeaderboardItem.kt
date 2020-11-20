package com.leirach.acentos.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.max

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
            val score = aciertos*100 - tiempo * 2
            return max(score, 0) //min 0 score
        }
    }
}
