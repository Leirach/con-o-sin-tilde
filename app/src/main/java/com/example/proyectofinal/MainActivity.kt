package com.example.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

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
    lateinit var image: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boton_juego = findViewById<Button>(R.id.button6)
        boton_izq = findViewById<Button>(R.id.left)
        boton_der = findViewById<Button>(R.id.right)
        image = findViewById(R.id.logoTema)

        val arregloJuegos = arrayOf<Int>(1, 2, 3)
        var index: Int = 0;


        boton_juego.setOnClickListener {
            Log.d("TAG", "Hallo")
            val juego_numero = arregloJuegos[index]
            val intent = Intent(this, ActivityJuegos::class.java).apply {
            putExtra("com.example.MainActivity2.extra.MESSAGE", juego_numero)
            }
            startActivityForResult(intent, TEXT_REQUEST)
        }

        boton_izq.setOnClickListener(View.OnClickListener {view ->
            if (index == 0) {
                index = 2
            } else {
                index--
            }


            if (index == 0) {
                image.setImageResource(R.drawable.sega_logo);
            } else if (index == 1) {
                image.setImageResource(R.drawable.ejercito_mexicano);
            } else if (index == 2) {
                image.setImageResource(R.drawable.watermelon);
            }
        })

        boton_der.setOnClickListener(View.OnClickListener { view ->
            if (index == 2) {
                index = 0
            } else {
                index++
            }

            if (index == 0) {
                image.setImageResource(R.drawable.sega_logo);
            } else if (index == 1) {
                image.setImageResource(R.drawable.ejercito_mexicano);
            } else if (index == 2) {
                image.setImageResource(R.drawable.watermelon);
            }


        })




    }







}