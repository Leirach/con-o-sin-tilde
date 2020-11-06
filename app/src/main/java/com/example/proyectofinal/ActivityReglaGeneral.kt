package com.example.proyectofinal

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.db.AcentosViewModel
import com.example.proyectofinal.db.ReglaGeneral
import kotlinx.android.synthetic.main.activity_regla_general.*

class ActivityReglaGeneral : AppCompatActivity() {
    lateinit var wordContainer: LinearLayout
    var selected = -1

    var word = listOf<String>("si", "la", "ba") // syllables
    var viewIds = arrayOf<Int>()                // ids of corresponding syllables in layout

    lateinit var wordList: List<ReglaGeneral> // word list
    var curIndex = 0
    lateinit var stopwatch: Chronometer
    lateinit var tildePrompt: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regla_general)

        stopwatch = findViewById(R.id.chronometer)
        tildePrompt = findViewById(R.id.tildePrompt)
        tildePrompt.visibility = View.GONE
        wordContainer = findViewById(R.id.wordContainer)

        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AcentosViewModel::class.java)
        dbViewModel.words.observe(this, Observer { words ->
            wordList = words // get words from db
            setWord()
            stopwatch.start()
        })
    }

    // set word in layout according to current index
    private fun setWord() {
        word = wordList[curIndex].syllable.split(' ') // get next word

        // reset
        selected = -1
        viewIds = arrayOf()
        wordContainer.removeAllViews() //clear linear layout

        //reusable parameters for inserting view
        val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0F)
        word.forEachIndexed { idx, elem ->    // forEach syllable
            val syllable = TextView(this)
            syllable.textSize = 50f
            syllable.gravity = Gravity.CENTER
            syllable.text = elem
            syllable.id = View.generateViewId()
            viewIds += syllable.id
            syllable.setOnClickListener {
                updateSelected(idx)
            }
            //push view in linear layout
            wordContainer.addView(syllable, param)
        }
    }

    // updates selected syllable in view
    private fun updateSelected(idx: Int) {
        // if previously selected, get previous and reduce text size
        if (selected != -1) {
            val prev: TextView = findViewById(viewIds[selected])
            prev.textSize = 50f
        }

        val selectedView: TextView = findViewById(viewIds[idx]) // get currently selected view from array
        selectedView.textSize = 70f
        selected = idx

        tildePrompt.visibility = View.VISIBLE //make tilde prompt visible
    }

    private fun gameEnd() {
        Toast.makeText(this, "TERMINÓ EL JUEGO", Toast.LENGTH_SHORT).show()
    }

    // checks if the selected answer is correct
    fun checkAnswer(view: View) {
        val tilde = (view.id == R.id.btnYes)
        val curWord = wordList[curIndex]
        val pos = word.size - curWord.pos

        Log.d("TAG", selected.toString())
        Log.d("TAG", pos.toString())
        Log.d("TAG", tilde.toString())
        Log.d("TAG", curWord.tilde.toString())

        if (selected == pos && tilde == curWord.tilde) {
            Toast.makeText(this, "CORRECTO", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "INCORRECTO", Toast.LENGTH_SHORT).show()
        }

        curIndex++
        if (curIndex >= 10) {
            gameEnd()
        }
        else {
            setWord()
        }
    }
}