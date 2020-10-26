package com.example.proyectofinal

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActivityReglaGeneral : AppCompatActivity() {
    lateinit var wordContainer: LinearLayout
    var selected = -1
    val word = arrayOf("si", "la", "ba") //get from db
    var viewIds = arrayOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regla_general)

        wordContainer = findViewById(R.id.wordContainer)
        val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0F)
        word.forEach { it ->
            val syllable = TextView(this)
            syllable.textSize = 50f
            syllable.gravity = Gravity.CENTER
            syllable.text = it
            syllable.id = View.generateViewId()
            viewIds += syllable.id
            syllable.setOnClickListener {
                updateSelected(it.id)
            }
            wordContainer.addView(syllable, param)
        }
    }

    private fun updateSelected(selIdx: Int) {
        if (selected != -1) {
            val prev: TextView = findViewById(this.selected)
            prev.textSize = 50f
        }

        val selected: TextView = findViewById(selIdx)
        selected.textSize = 70f

        this.selected = selIdx
    }
}