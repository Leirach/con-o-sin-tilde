package com.example.proyectofinal

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.db.AcentosViewModel
import com.example.proyectofinal.db.LeaderboardItem


class LeaderboardActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var viewLayoutManager: RecyclerView.LayoutManager
    var leaderboard = listOf<LeaderboardItem>()
    var filteredLeaderboard = listOf<LeaderboardItem>()
    lateinit var title: TextView

    // for selecting leaderboard database
    var selectedGame = 0
    private val gameTitles = arrayOf(R.string.regla_general, R.string.contexto, R.string.hiato)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        title = findViewById(R.id.game_name)

        // recycler setup
        selectedGame = intent.getIntExtra("com.example.extra.LEADERBOARD", 0)

        leaderboard = listOf(
            LeaderboardItem(0, System.currentTimeMillis()/1000,10,60),
            LeaderboardItem(1, System.currentTimeMillis()/1000,6,125)
        )

        val dbViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(AcentosViewModel::class.java)
        dbViewModel.leaderboard.observe(this, Observer { data ->
            leaderboard = data
            filterSelected()
            viewLayoutManager = LinearLayoutManager(this)
            viewAdapter = LeaderboardAdapter(this, filteredLeaderboard)
            recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
                adapter = viewAdapter
                layoutManager = viewLayoutManager
            }
        })
    }

    fun buttonClicked(view: View) {
        if (view.id == R.id.left) {
            if (selectedGame == 0) {
                selectedGame = 2 // max games
            } else {
                selectedGame--
            }
            val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_left)
            title.startAnimation(animation)
        }
        else if (view.id == R.id.right) {
            if (selectedGame == 2) {
                selectedGame = 0
            } else {
                selectedGame++
            }
            val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_right)
            title.startAnimation(animation)
        }

        filterSelected()
    }

    private fun filterSelected() {
        title.text = getString(gameTitles[selectedGame])

        filteredLeaderboard = leaderboard.filter {
            return@filter it.juego == selectedGame
        }
        if (::viewAdapter.isInitialized) {
            viewAdapter = LeaderboardAdapter(this, filteredLeaderboard)
            recyclerView.swapAdapter(viewAdapter, false)
        }

    }

}