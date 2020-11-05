package com.example.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.db.AcentosViewModel

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
        boton_leaderboard = findViewById<Button>(R.id.leaderboards)
        image = findViewById(R.id.logoTema)

        // ==== PRUEBA DB ====
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(AcentosViewModel::class.java)
        dbViewModel.words.observe(this, Observer { words ->
            val allwords = words.map { item ->
                return@map item.word
            }
            Log.d("TAG", allwords.joinToString(", "))
        })
        // ==== PRUEBA DB ====

        val arregloJuegos = arrayOf<Int>(R.id.sega, R.id.contexto, R.id.hiato)
        val images = arrayOf(R.drawable.sega_logo, R.drawable.ejercito_mexicano, R.drawable.watermelon)
        var index: Int = 0;


        boton_juego.setOnClickListener {
            Log.d("TAG", "Hallo")
            val juegoNumero = arregloJuegos[index]
            val intent = Intent(this, AnimationActivity::class.java).apply {
                putExtra("com.example.extra.GAME_MODE", juegoNumero)
            }
            startActivity(intent)
            // startActivityForResult(intent, TEXT_REQUEST)
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

            image.setImageResource(images[index])
        })

        boton_leaderboard.setOnClickListener({view  ->
            Log.d("TAG", "Hallo Leaderboard")
            val intent = Intent(this, Leaderboard::class.java).apply {
                putExtra("com.example.extra.LEADERBOARD", "String")
            }
            startActivity(intent)
            // startActivityForResult(intent, TEXT_REQUEST)
        })




    }







}