package com.leirach.acentos

import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
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
import com.leirach.acentos.db.AcentosViewModel
import com.leirach.acentos.db.LeaderboardItem
import com.leirach.acentos.db.ReglaGeneral
import kotlinx.android.synthetic.main.activity_regla_general.*
import kotlinx.coroutines.*

class ActivityReglaGeneral : AppCompatActivity(), GameEndDialogHandler {
    //constants
    val SIZE_LARGE = 50f
    val SIZE_SMALL = 30f
    // views
    lateinit var wordContainer: LinearLayout
    lateinit var stopwatch: Chronometer
    lateinit var tildePrompt: Group
    lateinit var aciertosTextView: TextView
    lateinit var instructions: TextView

    var selected = -1
    var word = listOf<String>("si", "la", "ba") // syllables
    var viewIds = arrayOf<Int>()                // ids of corresponding syllables in layout

    lateinit var wordList: List<ReglaGeneral> // word list
    var curIndex = 0
    var aciertos = 0

    private var wordSize = SIZE_LARGE // current word size

    // audio feedback
    lateinit var correctAudio: MediaPlayer
    lateinit var wrongAudio: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regla_general)

        // init views
        stopwatch = findViewById(R.id.chronometer)
        tildePrompt = findViewById(R.id.tildePrompt)
        tildePrompt.visibility = View.GONE
        wordContainer = findViewById(R.id.wordContainer)
        aciertosTextView = findViewById(R.id.textViewAciertos)
        instructions = findViewById(R.id.instructions)

        correctAudio = MediaPlayer.create(this, R.raw.correct)
        wrongAudio = MediaPlayer.create(this, R.raw.wrong)

        startGame()
    }

    // ===== Game logic methods =====
    private fun startGame() {
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AcentosViewModel::class.java
        )
        dbViewModel.getRandomReglaGeneral()
        dbViewModel.rgRandom.observe(this, Observer { words ->
            wordList = words // get words from db
            setWord()
            stopwatch.start()
            instructions.visibility = View.VISIBLE
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

        wordSize = if (wordList[curIndex].word.length >= 9) SIZE_SMALL else SIZE_LARGE // switches syllable size depending on word length
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
        bundle.putInt("GAME_INDEX", 0)

        //store best score in databse
        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(AcentosViewModel::class.java)
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
        instructions.visibility = View.INVISIBLE
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
        var delayTime: Long = 1000
        val correctWord = showCorrectAnswer(curWord.word) //get reference to correct answer TextView

        if (selected == pos && tilde == curWord.tilde) {
            aciertos++
            textViewAciertos.text = "$aciertos/10"
            // user feedback
            delayTime = 600
            correctAudio.start()
            correctWord.setTextColor(ContextCompat.getColor(this, R.color.success)) //green text correct answer
        }
        else {
            delayTime = 1000
            wrongAudio.start()
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