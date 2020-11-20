package com.leirach.acentos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AnimationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        var number: Int = intent.getIntExtra("com.example.extra.GAME_MODE", 0)
        val btnContinue = findViewById<Button>(R.id.continuar)

        btnContinue.setOnClickListener {
            when (number) {
                R.id.regla_general -> {
                    val intent = Intent(this, ActivityReglaGeneral::class.java)
                    startActivity(intent)
                }
                R.id.contexto -> {
                    val intent = Intent(this, ActivityContext::class.java)
                    startActivity(intent)
                }
                R.id.hiato -> {
                    val intent = Intent(this, ActivityHiato::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}