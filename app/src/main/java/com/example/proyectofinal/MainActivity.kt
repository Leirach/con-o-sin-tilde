package com.example.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    companion object {
        const val TEXT_REQUEST = 1
    }

    lateinit var boton_juego: Button
    lateinit var boton_izq: Button
    lateinit var boton_der: Button
    lateinit var boton_leaderboard: Button
    lateinit var boton_anima: Button
    lateinit var boton_texto: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun pressButtonHello(view: View) {
        Log.d("TAG", "Hallo")

        val intent = Intent(this, MainActivity2::class.java).apply {
            putExtra("com.example.MainActivity2.extra.MESSAGE", number)
        }
        startActivityForResult(intent, TEXT_REQUEST)
    }





}