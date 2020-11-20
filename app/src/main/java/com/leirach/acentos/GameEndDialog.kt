package com.leirach.acentos

import android.app.AlertDialog
import android.app.Dialog
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.leirach.acentos.db.LeaderboardItem


class GameEndDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            // Inflater returns the defined layout's view
            val view = inflater.inflate(R.layout.dialog_game_end, null)

            val timeText = arguments?.getCharSequence("TIEMPO") // time for display
            val time = arguments?.getInt("ELAPSEDTIME") // time for calculating score
            val aciertos = arguments?.getInt("ACIERTOS") // for calculating score
            val game = arguments?.getInt("GAME_INDEX") // for routing to the correct leaderboard
            var score: Int = 0
            if (time != null && aciertos != null)
                score = LeaderboardItem.calculateScore(aciertos, time)
            val total = 10

            //set time
            val timeTextView: TextView = view.findViewById(R.id.dialog_tiempo)
            timeTextView.text = resources.getString(R.string.dialog_tiempo, timeText)

            //set aciertos
            val aciertosTextView: TextView = view.findViewById(R.id.dialog_aciertos)
            aciertosTextView.text = resources.getString(R.string.dialog_aciertos, aciertos, total);

            //set score
            val ptsTextView: TextView = view.findViewById(R.id.dialog_pts)
            ptsTextView.text = Html.fromHtml(resources.getString(R.string.dialog_pts, score), HtmlCompat.FROM_HTML_MODE_COMPACT)

            // show banner
            val bannerTextView: TextView = view.findViewById(R.id.dialog_result_banner)
            bannerTextView.text = if (aciertos!! < 7)
                            resources.getString(R.string.dialog_result_bad)
                         else
                            resources.getString(R.string.dialog_result_good)

            val btnHome: ImageButton = view.findViewById(R.id.dialog_home)
            btnHome.setOnClickListener {
                val intent = Intent(activity, MainActivity::class.java).apply {
                    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }

            val btnLeaderboard: ImageButton = view.findViewById(R.id.dialog_leaderboards)
            btnLeaderboard.setOnClickListener {
                val intent = Intent(activity, LeaderboardActivity::class.java).apply {
                    putExtra("com.example.extra.LEADERBOARD", game)
                }
                TaskStackBuilder.create(activity)
                    .addNextIntentWithParentStack(intent)
                    .startActivities()
            }

            val btnReplay: ImageButton = view.findViewById(R.id.dialog_replay)
            btnReplay.setOnClickListener {
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
