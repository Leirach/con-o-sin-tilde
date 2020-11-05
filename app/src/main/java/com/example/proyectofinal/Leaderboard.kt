package com.example.proyectofinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Leaderboard : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var viewLayoutManager: RecyclerView.LayoutManager
    var leaderboard_items = mutableListOf<leaderboard_item>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        var number: Int = intent.getIntExtra("com.example.extra.LEADERBOARD", 0)
        leaderboard_items.add(leaderboard_item(1,2,3,4,5))
        leaderboard_items.add(leaderboard_item(6,7,8,9,10))
        viewLayoutManager = LinearLayoutManager(this@Leaderboard)
        viewAdapter = Adapter(this@Leaderboard, leaderboard_items)


        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = viewAdapter
            layoutManager = viewLayoutManager
        }

    }


}