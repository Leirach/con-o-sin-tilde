package com.example.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.db.AcentosViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        const val TEXT_REQUEST = 1
    }

    lateinit var boton_juego: Button
    lateinit var boton_izq: ImageButton
    lateinit var boton_der: ImageButton
    lateinit var boton_info: ImageButton
    lateinit var title: TextView
    lateinit var boton_leaderboard: ImageButton
    lateinit var boton_animation: ImageButton
    lateinit var boton_texto: ImageButton
    lateinit var image: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("TAG", "WHAT THE ACTUAL FUCK")
        boton_juego = findViewById(R.id.button6)
        boton_izq = findViewById(R.id.left)
        boton_der = findViewById(R.id.right)
        boton_leaderboard = findViewById(R.id.leaderboards)
        image = findViewById(R.id.logoTema)
        title = findViewById(R.id.game_name)
        boton_info = findViewById(R.id.btn_info)

        // ==== PRUEBA DB ====
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(AcentosViewModel::class.java)
        dbViewModel.words.observe(this, Observer { words ->
            val allwords = words.map { item ->
                return@map item.word
            }
            Log.d("TAG", allwords.joinToString(", "))
        })
        // ==== PRUEBA DB ====

        val arregloJuegos = arrayOf<Int>(R.id.regla_general, R.id.contexto, R.id.hiato)
        val images = arrayOf(R.drawable.sega_logo, R.drawable.ejercito_mexicano, R.drawable.watermelon)
        val gameTitles = arrayOf(R.string.regla_general, R.string.contexto, R.string.hiato)
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
                index = 2 // max games
            } else {
                index--
            }

            title.text = getString(gameTitles[index])
            val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_left)
            title.startAnimation(animation)
            image.setImageResource(images[index])
        })

        boton_der.setOnClickListener(View.OnClickListener { view ->
            if (index == 2) {
                index = 0
            } else {
                index++
            }

            title.text = getString(gameTitles[index])
            val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_right)
            title.startAnimation(animation)
            image.setImageResource(images[index])
        })

        boton_leaderboard.setOnClickListener { view ->
            Log.d("TAG", "Hallo Leaderboard")
            val intent = Intent(this, LeaderboardActivity::class.java).apply {
                putExtra("com.example.extra.LEADERBOARD", index)
            }
            startActivity(intent)
            // startActivityForResult(intent, TEXT_REQUEST)

        }

        boton_info.setOnClickListener {
            val intent = Intent(this, CreditosActivity::class.java)
            startActivity(intent)
        }

    }


}