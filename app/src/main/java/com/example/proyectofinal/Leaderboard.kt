package com.example.proyectofinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.db.LeaderboardItem


class Leaderboard : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var viewLayoutManager: RecyclerView.LayoutManager
    var leaderboard_items = mutableListOf<LeaderboardItem>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        var number: Int = intent.getIntExtra("com.example.extra.LEADERBOARD", 0)
        leaderboard_items.add(LeaderboardItem(2,10,60))
        leaderboard_items.add(LeaderboardItem(7,5,60))
        viewLayoutManager = LinearLayoutManager(this@Leaderboard)
        viewAdapter = Adapter(this@Leaderboard, leaderboard_items)


        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = viewAdapter
            layoutManager = viewLayoutManager
        }

    }


}