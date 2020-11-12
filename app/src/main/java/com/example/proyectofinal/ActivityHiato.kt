package com.example.proyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.db.AcentosViewModel
import com.example.proyectofinal.db.LeaderboardItem
import com.example.proyectofinal.db.ReglaGeneral

import com.example.proyectofinal.db.Hiato
import kotlinx.android.synthetic.main.activity_regla_general.*

class ActivityHiato : AppCompatActivity(), GameEndDialogHandler {
    //constants
    val SIZE_LARGE = 50f
    val SIZE_SMALL = 30f
    // views
    lateinit var wordContainer: LinearLayout
    lateinit var stopwatch: Chronometer
    lateinit var tildePrompt: Group
    lateinit var aciertosTextView: TextView
    lateinit var boton_sumar: Button
    lateinit var boton_restar: Button
    lateinit var boton_enviar: Button
    lateinit var contador: TextView
    lateinit var seleciona_letra_tilde: TextView
    var tilde_flag: Boolean = false

    var selected = -1
    var word = listOf<String>("San dia a") // syllables
    var viewIds = arrayOf<Int>()                // ids of corresponding syllables in layout

    lateinit var wordList: MutableList<Hiato> // word list
//    = mutableListOf<ReglaHiato>(ReglaHiato("Sandia", "San dia a", 3, true, 2))

    var curIndex = 0
    var aciertos = 0

    private var wordSize = SIZE_LARGE // current word size

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiato)

        // init views
        stopwatch = findViewById(R.id.chronometer)
        tildePrompt = findViewById(R.id.tildePrompt)
        tildePrompt.visibility = View.GONE
        wordContainer = findViewById(R.id.wordContainer)
        aciertosTextView = findViewById(R.id.textViewAciertos)

        boton_sumar = findViewById(R.id.sumar)
        boton_restar = findViewById(R.id.restar)
//        boton_enviar = findViewById(R.id.submit)
        var variable: Int

        seleciona_letra_tilde = findViewById(R.id.seleciona_letra_tilde)
        seleciona_letra_tilde.visibility = View.INVISIBLE

        boton_sumar.setOnClickListener({
            contador = findViewById(R.id.counter)
            variable = contador.text.toString().toInt()
            if (variable < 6) {
                variable += 1
                contador.setText((variable.toString()))
                tildePrompt.visibility = View.VISIBLE
            }
        })

        boton_restar.setOnClickListener({
            contador = findViewById(R.id.counter)
            variable = contador.text.toString().toInt()
            if (variable > 0) {
                variable -= 1
                contador.setText((variable.toString()))
                tildePrompt.visibility = View.VISIBLE
            }
        })


        startGame()
    }

    // ===== Game logic methods =====
    private fun startGame() {
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AcentosViewModel::class.java
        )
//        dbViewModel.getRandomReglaGeneral()
//        dbViewModel.randomReglaGeneral.observe(this, Observer { words ->
//            wordList = words // get words from db
//            setWord()
//            stopwatch.start()
//        })
        wordList = mutableListOf<Hiato>()
        wordList.clear()
        wordList.add(Hiato("Sandia", "San di a", 3, true, 2))
        wordList.add(Hiato("Adios", "A dios", 2, false, -1))
        wordList.add(Hiato("Sandia", "San di a", 3, true, 2))
        wordList.add(Hiato("Adios", "A dios", 3, false, 2))
        wordList.add(Hiato("Cueva", "Cue va", 2, false, -1))
        wordList.add(Hiato("Sandia", "San di a", 3, true, 2))
        wordList.add(Hiato("Adios", "A dios", 2, false, -1))
        wordList.add(Hiato("Sandia", "San di a", 3, true, 2))
        wordList.add(Hiato("Adios", "A dios", 3, false, 2))
        wordList.add(Hiato("Cueva", "Cue va", 2, false, -1))
        setWord()
        stopwatch.start()
    }

    // set word in layout according to current index
    private fun setWord() {
        tilde_flag = false;
        seleciona_letra_tilde = findViewById(R.id.seleciona_letra_tilde)
        seleciona_letra_tilde.visibility = View.INVISIBLE
        word = wordList[curIndex].word.chunked(1) // get next word

        // reset
        selected = -1
        viewIds = arrayOf()
        wordContainer.removeAllViews() //clear linear layout

        //reusable parameters for inserting view

        wordSize = if (wordList[curIndex].word.length >= 10) SIZE_SMALL else SIZE_LARGE // switches syllable size depending on word length
        val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        word.forEachIndexed { idx, elem ->    // forEach syllable
            val syllable = TextView(this)
            syllable.setTextSize(TypedValue.COMPLEX_UNIT_SP, wordSize)
            syllable.gravity = Gravity.CENTER
            syllable.text = elem
            syllable.id = View.generateViewId()
            syllable.isSingleLine = true
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
        val elapsedTime = ((SystemClock.elapsedRealtime() - stopwatch.base) / 1000).toInt() //elapse time as seconds
        val dialog = GameEndDialog()
        dialog.isCancelable = false
        val bundle = Bundle()
        bundle.putCharSequence("TIEMPO", stopwatch.text) // time as string
        bundle.putInt("ELAPSEDTIME", elapsedTime)   // time as int
        bundle.putInt("ACIERTOS", aciertos)

        //store best score in databse
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AcentosViewModel::class.java)
        dbViewModel.leaderboardInsert(LeaderboardItem(0, System.currentTimeMillis(), aciertos, elapsedTime))

        //start dialog with bundled data
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
        if (tilde_flag) {

            if (selected != -1) {
                val prev: TextView = findViewById(viewIds[selected])
                prev.setTextSize(TypedValue.COMPLEX_UNIT_SP, wordSize)
                prev.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.default_text_color
                    )
                ) // change color
                val param =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                prev.layoutParams = param
            }

            val selectedView: TextView =
                findViewById(viewIds[idx]) // get currently selected view from array
            selectedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, wordSize + 20) //increase text size
            selectedView.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimary
                )
            ) // change color
            val param = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.7f
            ) //increase container size
            selectedView.layoutParams = param
            selected = idx
            checkAnswerTilde()
        }

    }

    // checks if the selected answer is correct
    fun checkAnswer(view: View) {
        contador = findViewById(R.id.counter)
        val tilde = (view.id == R.id.btnYes) // true = user clicked yes, false = user clicked no
        val curWord = wordList[curIndex]

        if (!curWord.tilde) {
            if (contador.text.toString().toInt() == curWord.syllableCount && !tilde) {
                aciertos++
                textViewAciertos.text = "$aciertos/10"
            }
        } else {
            if (tilde) { // new question
                tilde_flag = true;
                seleciona_letra_tilde = findViewById(R.id.seleciona_letra_tilde)
                seleciona_letra_tilde.visibility = View.VISIBLE
                return
            }
        }
        tildePrompt.visibility = View.GONE

        curIndex++
        if (curIndex >= 10) {
            gameEnd()
        } else {
            setWord()
        }

    }


    fun checkAnswerTilde() {
        val curWord = wordList[curIndex]
        val pos = word.size - curWord.pos

        if (selected == pos) {
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
