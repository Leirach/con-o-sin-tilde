package com.example.proyectofinal

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.TaskStackBuilder
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment


class GameEndDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            // Inflater returns the defined layout's view
            val view = inflater.inflate(R.layout.dialog_game_end, null)

            val time = arguments?.getCharSequence("TIEMPO" )
            val aciertos = arguments?.getInt("ACIERTOS")
            val total = 10

            val timeTextView: TextView = view.findViewById(R.id.dialog_tiempo)
            timeTextView.text = resources.getString(R.string.dialog_tiempo, time)

            val aciertosTextView: TextView = view.findViewById(R.id.dialog_aciertos)
            aciertosTextView.text = resources.getString(R.string.dialog_aciertos, aciertos, total);


            val btnHome: ImageButton = view.findViewById(R.id.dialog_home)
            btnHome.setOnClickListener{
                val intent = Intent(activity, MainActivity::class.java).apply {
                    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }

            val btnLeaderboard: ImageButton = view.findViewById(R.id.dialog_leaderboards)
            btnLeaderboard.setOnClickListener{
                val intent = Intent(activity, Leaderboard::class.java)
                TaskStackBuilder.create(activity)
                    .addNextIntentWithParentStack(intent)
                    .startActivities()
            }

            val btnReplay: ImageButton = view.findViewById(R.id.dialog_replay)
            btnReplay.setOnClickListener{
                dialog?.setOnCancelListener {
                    (activity as GameEndDialogHandler).handleOnCancel()
                }
                dialog?.cancel()
            }

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
