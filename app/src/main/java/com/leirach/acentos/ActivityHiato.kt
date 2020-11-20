package com.leirach.acentos

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.leirach.acentos.db.AcentosViewModel
import com.leirach.acentos.db.LeaderboardItem

import com.leirach.acentos.db.Hiato
import kotlinx.android.synthetic.main.activity_regla_general.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    lateinit var counterPrompt: Group
    var tilde_flag: Boolean = false

    var selected = -1
    var word = listOf<String>("San dia a") // syllables
    var viewIds = arrayOf<Int>()                // ids of corresponding syllables in layout

    lateinit var wordList: List<Hiato> // word list

    var curIndex = 0
    var aciertos = 0

    private var wordSize = SIZE_LARGE // current word size

    // audio feedback
    lateinit var correctAudio: MediaPlayer
    lateinit var wrongAudio: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiato)

        // init views
        stopwatch = findViewById(R.id.chronometer)
        tildePrompt = findViewById(R.id.tildePrompt)
        tildePrompt.visibility = View.GONE
        wordContainer = findViewById(R.id.wordContainer)
        aciertosTextView = findViewById(R.id.textViewAciertos)
        contador = findViewById(R.id.counter)
        boton_sumar = findViewById(R.id.sumar)
        boton_restar = findViewById(R.id.restar)
        seleciona_letra_tilde = findViewById(R.id.seleciona_letra_tilde)
        contador = findViewById(R.id.counter)
        counterPrompt = findViewById(R.id.counterPrompt)

        // audio
        correctAudio = MediaPlayer.create(this, R.raw.correct)
        wrongAudio = MediaPlayer.create(this, R.raw.wrong)

        var variable: Int // syllable counter
        seleciona_letra_tilde.visibility = View.INVISIBLE

        boton_sumar.setOnClickListener {

            variable = contador.text.toString().toInt()
            if (variable < 6) {
                variable += 1
                contador.text = variable.toString()
                tildePrompt.visibility = View.VISIBLE
            }
        }

        boton_restar.setOnClickListener {
            variable = contador.text.toString().toInt()
            if (variable > 0) {
                variable -= 1
                contador.text = variable.toString()
                tildePrompt.visibility = View.VISIBLE
            }
        }


        startGame()
    }

    // ===== Game logic methods =====
    private fun startGame() {
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AcentosViewModel::class.java
        )
        dbViewModel.getRandomHiato()
        dbViewModel.hiatoRandom.observe(this, Observer { words ->
            wordList = words // get words from db
            Log.d("TAG", wordList.toString())
            setWord()
            stopwatch.start()
        })
    }

    // set word in layout according to current index
    private fun setWord() {
        contador.text = "0"
        tilde_flag = false;
        seleciona_letra_tilde.visibility = View.INVISIBLE
        counterPrompt.visibility = View.VISIBLE
        word = wordList[curIndex].word.chunked(1) // get next word

        // reset
        selected = -1
        viewIds = arrayOf()
        wordContainer.removeAllViews() //clear linear layout
        wordContainer.updateLayoutParams<ConstraintLayout.LayoutParams> {
            verticalBias = 0.3F
        }

        //reusable parameters for inserting view

        wordSize = if (wordList[curIndex].syllable.length >= 9) SIZE_SMALL else SIZE_LARGE // switches syllable size depending on word length
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
        bundle.putInt("GAME_INDEX", 2)

        //store best score in databse
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AcentosViewModel::class.java)
        dbViewModel.leaderboardInsert(LeaderboardItem(2, System.currentTimeMillis(), aciertos, elapsedTime))

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
            selected = idx
            checkAnswerTilde()
        }

    }


    // checks if the selected answer is correct
    fun checkAnswer(view: View) {
        val tilde = (view.id == R.id.btnYes) // true = user clicked yes, false = user clicked no
        val curWord = wordList[curIndex]
        var delayTime: Long

        // cantidad de silabas correctas y respondio lleva tilde correcto
        if (contador.text.toString().toInt() == curWord.syllableCount && curWord.tilde == tilde) {
            if (curWord.tilde) { // si lleva tilde, preguntar en cual letra
                tilde_flag = true;
                seleciona_letra_tilde.visibility = View.VISIBLE
                counterPrompt.visibility = View.GONE
                tildePrompt.visibility = View.GONE
                wordContainer.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    verticalBias = 0.5F
                }
                return // RETURN PARA NO SALTARSE LA PREGUNTA
            }
            else { // palabra no lleva tilde, marcar correcta
                correctAudio.start()
                val correctWord = showCorrectAnswer(curWord.syllable)
                correctWord.setTextColor(ContextCompat.getColor(this, R.color.success)) //green text correct answer
                delayTime = 600
                aciertos++
                textViewAciertos.text = "$aciertos/10"
            }
        }
        else { // cantidad de silabas mal o tilde mal
            wrongAudio.start()
            val correctWord = showCorrectAnswer(curWord.syllable)
            delayTime = 1000
            correctWord.setTextColor(ContextCompat.getColor(this, R.color.error)) //red text bad answer
        }

        tildePrompt.visibility = View.GONE

        GlobalScope.launch(context = Dispatchers.Main) {
            delay(delayTime)
            nextQuestion()
        }
    }


    private fun checkAnswerTilde() {
        val curWord = wordList[curIndex]
        val pos = word.size - curWord.pos
        var delayTime: Long

        if (selected == pos) {
            correctAudio.start()
            val correctWord = showCorrectAnswer(curWord.syllable)
            correctWord.setTextColor(ContextCompat.getColor(this, R.color.success)) //green text correct answer
            delayTime = 600
            aciertos++
            textViewAciertos.text = "$aciertos/10"
        }
        else {
            wrongAudio.start()
            val correctWord = showCorrectAnswer(curWord.syllable)
            delayTime = 1000
            correctWord.setTextColor(ContextCompat.getColor(this, R.color.error)) //red text bad answer
        }

        tildePrompt.visibility = View.GONE

        GlobalScope.launch(context = Dispatchers.Main) {
            delay(delayTime)
            nextQuestion()
        }
    }

    private fun showCorrectAnswer(word: String): TextView {
        wordContainer.removeAllViews()
        val correctWord = TextView(this) // replaces main linearlayout with the correct word
        correctWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, wordSize+20)
        correctWord.gravity = Gravity.CENTER
        correctWord.text = word
        correctWord.id = View.generateViewId()
        correctWord.isSingleLine = true
        wordContainer.addView(correctWord)
        return correctWord
    }

    private fun nextQuestion() {
        curIndex++
        if (curIndex >= 10) {
            gameEnd()
        } else {
            setWord()
        }
    }
}
