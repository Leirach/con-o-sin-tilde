package com.example.proyectofinal

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ExplanationActivity : AppCompatActivity() {
    var selectedGame = 0
    lateinit var title: TextView
    lateinit var scrollView: ScrollView
    private val gameTitles = arrayOf(R.string.regla_general, R.string.contexto, R.string.hiato)
    private val layouts = arrayOf(R.layout.explanation_regla_general, R.layout.explanation_contexto, R.layout.explanation_hiatos)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explanation)
        // init views
        title = findViewById(R.id.explanation_title)
        scrollView = findViewById(R.id.explanation_container)

        selectedGame = intent.getIntExtra("com.example.extra.GAME_MODE", 0)

        setScrollViewContent()
    }

    fun setScrollViewContent() {
        title.text = getString(gameTitles[selectedGame])
        scrollView.removeAllViews()
        val layoutId = layouts[selectedGame]
        val child: View = layoutInflater.inflate(layoutId, null)
        scrollView.addView(child)
    }

    fun buttonClicked(view: View) {
        if (view.id == R.id.left) {
            if (selectedGame == 0) {
                selectedGame = 2 // max games
            } else {
                selectedGame--
            }
            val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.left_in)
            title.startAnimation(animation)
        }
        else if (view.id == R.id.right) {
            if (selectedGame == 2) {
                selectedGame = 0
            } else {
                selectedGame++
            }
            val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.right_in)
            title.startAnimation(animation)
        }

        setScrollViewContent()
    }
}