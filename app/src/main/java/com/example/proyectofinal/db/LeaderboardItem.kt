package com.example.proyectofinal.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaderboard")
class LeaderboardItem(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var fecha: Int,
    var aciertos: Int,
    var tiempo: Int,
    var score: Int) {
    constructor(fecha: Int, aciertos: Int, tiempo: Int): this(0, fecha, aciertos, tiempo, aciertos*1000/tiempo)

}
