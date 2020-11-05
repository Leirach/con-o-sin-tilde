package com.example.proyectofinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class Adapter(private val context: Context, private val leaderboard_items: List<leaderboard_item>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

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

    override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
        val  item = leaderboard_items[position]
        holder.lugar.text = item.lugar.toString()
        holder.fecha.text = item.fecha.toString()
        holder.aciertos.text = item.aciertos.toString()
        holder.tiempo.text = item.tiempo.toString()
        holder.score.text = item.score.toString()
    }

    override fun getItemCount(): Int = leaderboard_items.size



}