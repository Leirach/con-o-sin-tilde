package com.example.proyectofinal

import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Chronometer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.db.AcentosViewModel
import com.example.proyectofinal.db.ReglaGeneral
import kotlinx.android.synthetic.main.activity_regla_general.*
import kotlin.time.seconds

const val SIZE_LARGE = 50f
const val SIZE_SMALL = 25f

class ActivityReglaGeneral : AppCompatActivity(), GameEndDialogHandler {
    // views
    lateinit var wordContainer: LinearLayout
    lateinit var stopwatch: Chronometer
    lateinit var tildePrompt: Group
    lateinit var aciertosTextView: TextView

    var selected = -1
    var word = listOf<String>("si", "la", "ba") // syllables
    var viewIds = arrayOf<Int>()                // ids of corresponding syllables in layout

    lateinit var wordList: List<ReglaGeneral> // word list
    var curIndex = 0
    var aciertos = 0

    private var wordSize = SIZE_LARGE // current word size

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regla_general)

        // init views
        stopwatch = findViewById(R.id.chronometer)
        tildePrompt = findViewById(R.id.tildePrompt)
        tildePrompt.visibility = View.GONE
        wordContainer = findViewById(R.id.wordContainer)
        aciertosTextView = findViewById(R.id.textViewAciertos)

        startGame()
    }

    // ===== Game logic methods =====
    private fun startGame() {
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AcentosViewModel::class.java
        )
        dbViewModel.getRandomReglaGeneral()
        dbViewModel.randomReglaGeneral.observe(this, Observer { words ->
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

        wordSize = if (word.size >= 5) SIZE_SMALL else SIZE_LARGE // switches syllable size depending on word length
        val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        word.forEachIndexed { idx, elem ->    // forEach syllable
            val syllable = TextView(this)
            syllable.setTextSize(TypedValue.COMPLEX_UNIT_SP, wordSize)
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

    private fun gameEnd() {
        stopwatch.stop()
        val elapsedTime = ((SystemClock.elapsedRealtime() - stopwatch.base) * 1000).toInt()
        val dialog = GameEndDialog()
        dialog.isCancelable = false
        val bundle = Bundle()
        bundle.putCharSequence("TIEMPO", stopwatch.text)
        bundle.putInt("ACIERTOS", aciertos)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "DialogEndGame")
    }

    private fun resetGame() {
        curIndex = 0
        aciertos = 0
        textViewAciertos.text = "0/10"
        stopwatch.base = SystemClock.elapsedRealtime();
        startGame()
    }

    // GameEndDialogHandler methods
    override fun handleOnCancel() {
        resetGame()
    }

    // ===== Buttons/UI callbacks =====
    // updates selected syllable in view
    private fun updateSelected(idx: Int) {
        // if previously selected, get previous and reduce text size
        if (selected != -1) {
            val prev: TextView = findViewById(viewIds[selected])
            prev.setTextSize(TypedValue.COMPLEX_UNIT_SP, wordSize)
            prev.setTextColor(ContextCompat.getColor(this, R.color.default_text_color)) // change color
            val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            prev.layoutParams = param
        }

        val selectedView: TextView = findViewById(viewIds[idx]) // get currently selected view from array
        selectedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, wordSize+20) //increase text size
        selectedView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary)) // change color
        val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.7f) //increase container size
        selectedView.layoutParams = param
        selected = idx

        tildePrompt.visibility = View.VISIBLE //make tilde prompt visible
    }

    // checks if the selected answer is correct
    fun checkAnswer(view: View) {
        val tilde = (view.id == R.id.btnYes)
        val curWord = wordList[curIndex]
        val pos = word.size - curWord.pos

        if (selected == pos && tilde == curWord.tilde) {
            aciertos++
            textViewAciertos.text = "$aciertos/10"
        }

        tildePrompt.visibility = View.GONE

        curIndex++
        if (curIndex >= 10) {
            gameEnd()
        } else {
            setWord()
        }
    }
}