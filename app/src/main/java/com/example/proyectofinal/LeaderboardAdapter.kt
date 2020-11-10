package com.example.proyectofinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.db.LeaderboardItem
import java.text.SimpleDateFormat
import java.util.*


class LeaderboardAdapter(private val context: Context, val leaderboardItems: List<LeaderboardItem>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    val dateFormat = SimpleDateFormat.getDateInstance()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lugar: TextView = itemView.findViewById(R.id.Lugar)
        val fecha: TextView = itemView.findViewById(R.id.Fecha)
        val aciertos: TextView = itemView.findViewById(R.id.Aciertos)
        val tiempo: TextView = itemView.findViewById(R.id.Tiempo)
        val score: TextView = itemView.findViewById(R.id.Score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardAdapter.ViewHolder, position: Int) {
        val  item = leaderboardItems[position]
        holder.lugar.text = (position+1).toString()
        holder.fecha.text = dateFormat.format((item.fecha)).toString()
        holder.aciertos.text = "${item.aciertos}/10"
        val minutes = item.tiempo / 60
        val seconds = item.tiempo % 60
        holder.tiempo.text = String.format("%02d:%02d", minutes, seconds)
        holder.score.text = item.score.toString()
    }

    override fun getItemCount(): Int = leaderboardItems.size

}