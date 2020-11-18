package com.example.proyectofinal

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
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
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.proyectofinal.db.AcentosViewModel
import com.example.proyectofinal.db.Contexto
import com.example.proyectofinal.db.LeaderboardItem
import kotlinx.android.synthetic.main.activity_context.*//view
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//import kotlinx.android.synthetic.main.activity_regla_general.*


class ActivityContext : AppCompatActivity(),  GameEndDialogHandler {
    //constants
    // views
    lateinit var bOpt1: Button
    lateinit var bOpt2: Button
    lateinit var bOpt3: Button
    lateinit var stopwatch: Chronometer
    lateinit var SentencePrompt: TextView
    lateinit var aciertosTextView: TextView
    lateinit var defaultColor: Drawable


    //var word = listOf<String>("Sen ten ce") //sentence

    lateinit var wordList: List<Contexto> // word list
    var curIndex = 0
    var aciertos = 0

    // audio feedback
    lateinit var correctAudio: MediaPlayer
    lateinit var wrongAudio: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_context)

        // init views
        stopwatch = findViewById(R.id.chronometer)
        SentencePrompt = findViewById(R.id.Sentence)
        bOpt1 = findViewById(R.id.opt1)
        bOpt2 = findViewById(R.id.opt2)
        bOpt3 = findViewById(R.id.opt3)
        defaultColor = bOpt1.background

        aciertosTextView = findViewById(R.id.textViewAciertos)

        correctAudio = MediaPlayer.create(this, R.raw.correct)
        wrongAudio = MediaPlayer.create(this, R.raw.wrong)

        startGame()
    }

    // ===== Game logic methods =====
    private fun startGame() { //
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AcentosViewModel::class.java
        )
        dbViewModel.getRandomContexto()
       dbViewModel.contextRandom.observe(this, Observer { words ->
            wordList = words // get words from db
            setWord()
            stopwatch.start()
        })
    }

    // set word in layout according to current index
    private fun setWord() {
        //función
        val curWord = wordList[curIndex]
        bOpt1.background = defaultColor
        bOpt2.background = defaultColor
        bOpt3.background = defaultColor

        SentencePrompt.text = curWord.sentence
        bOpt1.text = curWord.opt1
        bOpt2.text = curWord.opt2
        if(curWord.opt3.isEmpty()){
            bOpt3.visibility = View.INVISIBLE
        }
        else {
            bOpt3.visibility = View.VISIBLE
            bOpt3.text = curWord.opt3
        }
        bOpt1.setOnClickListener{
            checkAnswer(1)
        }
        bOpt2.setOnClickListener{
            checkAnswer(2)
        }
        bOpt3.setOnClickListener{
            checkAnswer(3)
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
        bundle.putInt("GAME_INDEX", 1)

        //store best score in databse
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AcentosViewModel::class.java)
        dbViewModel.leaderboardInsert(LeaderboardItem(1, System.currentTimeMillis(), aciertos, elapsedTime))

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

    // checks if the selected answer is correct
    private fun checkAnswer(answer: Int) {

        val curWord = wordList[curIndex]
        var delayTime: Long
        if (answer == curWord.correct) {
            correctAudio.start()
            aciertos++
            textViewAciertos.text = "$aciertos/10"
            delayTime = 600
        }
        else {
            wrongAudio.start()
            delayTime = 1000
        }

        showCorrectAnswer(answer, curWord.correct)

        GlobalScope.launch(context = Dispatchers.Main) {
            delay(delayTime)
            nextQuestion()
        }
    }

    private fun showCorrectAnswer(selected: Int, correct: Int) {
        when (selected) {
            1 -> opt1.setBackgroundColor(ContextCompat.getColor(this, R.color.error))
            2 -> opt2.setBackgroundColor(ContextCompat.getColor(this, R.color.error))
            3 -> opt3.setBackgroundColor(ContextCompat.getColor(this, R.color.error))
        }
        when (correct) {
            1 -> opt1.setBackgroundColor(ContextCompat.getColor(this, R.color.success))
            2 -> opt2.setBackgroundColor(ContextCompat.getColor(this, R.color.success))
            3 -> opt3.setBackgroundColor(ContextCompat.getColor(this, R.color.success))
        }
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