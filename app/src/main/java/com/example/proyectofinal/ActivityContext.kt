package com.example.proyectofinal

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


    //var word = listOf<String>("Sen ten ce") //sentence

    lateinit var wordList: MutableList<Contexto> // word list
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
        //dbViewModel.getRandomContexto()
       /* dbViewModel.rgRandom.observe(this, Observer { words -> //Database, todavía no se encuentra
            wordList = words // get words from db
            setWord()
            stopwatch.start()
        })*/
        wordList = mutableListOf<Contexto>()
        wordList.clear()
        wordList.add(Contexto("ejemplo", "Opción 1", "Opción 2", "Opción 3", 1, "Elija la opción 1"))
        wordList.add(Contexto("ejemplo", "Opción 1", "Opción 2", "Opción 3", 2, "Elija la opción 2"))
        wordList.add(Contexto("ejemplo", "Opción 1", "Opción 2", "Opción 3", 3, "Elija la opción 3"))
        wordList.add(Contexto("opcion dum", "Opción 1", "Opción 2", "", 2, "Prueba para ocultar opciones"))
        wordList.add(Contexto("Como", "Comó", "Cómo", "Como", 3, "Yo _____ huevo en el desayuno"))
        wordList.add(Contexto("Porque", "Porque", "Porqué", "Por que", 2, "Lo digó ______ sí"))
        wordList.add(Contexto("Mas", "Más", "Mas", "Cualquiera de las dos", 1, "El juego cuesta 700 pesos ____ iva"))
        wordList.add(Contexto("Mas", "Más", "Mas", "Cualquiera de las dos", 2, "No hay ___ que pan aquí"))
        wordList.add(Contexto("Porque", "Porque", "Porqué", "Por que", 2, "¿_____ no nos dieron asueto?"))
        wordList.add(Contexto("Como", "Comó", "Cómo", "Como", 1, "¿_____ le va a usted?"))
        wordList.add(Contexto("Papa", "Papá", "Papa", "Cualquiera de las dos", 2, "El ______ de Antonio se enojo mucho"))
        wordList.add(Contexto("opcion dum", "Opción 1", "Opción 2", "Opción 3", 2, "Está opción extra es para que no truene el programa"))

        setWord()
        stopwatch.start()
    }

    // set word in layout according to current index
    private fun setWord() {
        //función
        val curWord = wordList[curIndex]

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
    fun checkAnswer(answer: Int) {

        val curWord = wordList[curIndex]
        if (answer == curWord.correct) {
            correctAudio.start()
            aciertos++
            textViewAciertos.text = "$aciertos/10"
        }
        else {
            wrongAudio.start()
        }


        curIndex++
        if (curIndex >= 10) {
            gameEnd()
        } else {
            setWord()
        }
    }
}