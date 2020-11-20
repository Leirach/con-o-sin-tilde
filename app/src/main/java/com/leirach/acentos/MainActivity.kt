package com.leirach.acentos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit


class MainActivity : AppCompatActivity() {

    companion object {
        const val TEXT_REQUEST = 1
    }

    lateinit var boton_juego: Button
    lateinit var boton_izq: ImageButton
    lateinit var boton_der: ImageButton
    lateinit var boton_info: ImageButton
    lateinit var boton_leaderboard: ImageButton
    lateinit var boton_animation: ImageButton
    lateinit var boton_texto: ImageButton
    lateinit var image: ImageView
    lateinit var sharedPref: SharedPreferences

    val arregloJuegos = arrayOf<Int>(R.id.regla_general, R.id.contexto, R.id.hiato)
    val images = arrayOf(R.drawable.regla_general, R.drawable.contexto, R.drawable.hiatos)
    val gameTitles = arrayOf(R.string.regla_general, R.string.contexto, R.string.hiato)
    var index: Int = 0;

    // for swipe gestures
    private val MIN_DISTANCE = 150f
    private var x1: Float = 0f
    private var x2: Float = 0f

    private var firstTimePlaying = arrayOf(true, true, true)
    private var preferenceKeys = arrayOf("regla_general", "contexto", "hiatos")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boton_juego = findViewById(R.id.button6)
        boton_izq = findViewById(R.id.left)
        boton_der = findViewById(R.id.right)
        boton_leaderboard = findViewById(R.id.leaderboards)
        image = findViewById(R.id.logoTema)
        //title = findViewById(R.id.game_name)
        boton_info = findViewById(R.id.btn_info)

        sharedPref = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE)
        firstTimePlaying[0] = sharedPref.getBoolean("regla_general", true)
        firstTimePlaying[1] = sharedPref.getBoolean("contexto", true)
        firstTimePlaying[2] = sharedPref.getBoolean("hiatos", true)

        // ==== PRUEBA DB ====
//        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(AcentosViewModel::class.java)
//        dbViewModel.rgRandom.observe(this, Observer { words ->
//            val allwords = words.map { item ->
//                return@map item.word
//            }
//            //Log.d("TAG", allwords.joinToString(", "))
//        })
        // ==== PRUEBA DB ====

        boton_juego.setOnClickListener {
            val juegoNumero = arregloJuegos[index]
            lateinit var nextIntent: Intent
            if (firstTimePlaying[index]) {
                nextIntent = Intent(this, AnimationActivity::class.java).apply {
                    putExtra("com.example.extra.GAME_MODE", juegoNumero)
                }
                sharedPref.edit {
                    this.putBoolean(preferenceKeys[index], false)
                    this.apply()
                }
            }
            else {
                when (juegoNumero) {
                    R.id.regla_general -> {
                        nextIntent = Intent(this, ActivityReglaGeneral::class.java)
                    }
                    R.id.contexto -> {
                        nextIntent = Intent(this, ActivityContext::class.java)
                    }
                    R.id.hiato -> {
                        nextIntent = Intent(this, ActivityHiato::class.java)
                    }
                }
            }

            startActivity(nextIntent)
            // startActivityForResult(intent, TEXT_REQUEST)
        }

        boton_izq.setOnClickListener { view ->
            left()
        }

        boton_der.setOnClickListener{ view ->
            right()
        }

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

    private fun left() {
        if (index == 0) {
            index = 2 // max games
        } else {
            index--
        }

        // title.text = getString(gameTitles[index])
        image.setImageResource(images[index])
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.left_in)
        // title.startAnimation(animation)
        image.startAnimation(animation)
    }

    fun right() {
        if (index == 2) {
            index = 0
        } else {
            index++
        }

        // title.text = getString(gameTitles[index])
        image.setImageResource(images[index])
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.right_in)
        // title.startAnimation(animation)
        image.startAnimation(animation)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> x1 = event.x
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                val deltaX: Float = x2 - x1
                if (deltaX > MIN_DISTANCE) {
                    left()
                } else if (deltaX < -MIN_DISTANCE) {
                    right()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    fun gotoAnimation(view: View) {
        val juegoNumero = arregloJuegos[index]
        val intent = Intent(this, AnimationActivity::class.java).apply {
            putExtra("com.example.extra.GAME_MODE", juegoNumero)
        }
        if (firstTimePlaying[index]) {
            sharedPref.edit {
                this.putBoolean(preferenceKeys[index], false)
                this.apply()
            }
        }
        startActivity(intent)
    }
    fun gotoExplanation(view: View) {
        val juegoNumero = index
        val intent = Intent(this, ExplanationActivity::class.java).apply {
            putExtra("com.example.extra.GAME_MODE", juegoNumero)
        }
        startActivity(intent)
    }


}