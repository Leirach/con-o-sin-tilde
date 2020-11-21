package com.leirach.acentos

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class AnimationActivity : AppCompatActivity() {
    lateinit var videoView: VideoView
    lateinit var btnContinue: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        toggleActionBar(resources.configuration.orientation) // hide/show action bar

        val gameNumber: Int = intent.getIntExtra("com.example.extra.GAME_MODE", 0)
        btnContinue = findViewById<Button>(R.id.continuar)
        videoView = findViewById(R.id.animation_video)


        val path = when (gameNumber) {
            R.id.regla_general -> {
                "android.resource://" + packageName + "/" + R.raw.regla_general
            }
            R.id.contexto -> {
                "android.resource://" + packageName + "/" + R.raw.contexto
            }
            R.id.hiato -> {
                "android.resource://" + packageName + "/" + R.raw.hiatos
            }
            else -> {
                "android.resource://" + packageName + "/" + R.raw.regla_general
            }
        }

        videoView.setVideoPath(path)
        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
        videoView.start()

        videoView.onfi

        btnContinue.setOnClickListener {
            when (gameNumber) {
                R.id.regla_general -> {
                    val intent = Intent(this, ActivityReglaGeneral::class.java)
                    startActivity(intent)
                }
                R.id.contexto -> {
                    val intent = Intent(this, ActivityContext::class.java)
                    startActivity(intent)
                }
                R.id.hiato -> {
                    val intent = Intent(this, ActivityHiato::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggleActionBar(newConfig.orientation)
    }

    // toggle action bar depending on sent orientation parameter
    fun toggleActionBar(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportActionBar?.hide()
        }
        else {
            supportActionBar?.show()
        }
    }

    // show hide skip button when screen is tapped
    fun screenTap(view: View) {
        if (btnContinue.visibility == View.VISIBLE){
            btnContinue.visibility = View.INVISIBLE
        }
        else {
            btnContinue.visibility = View.VISIBLE
        }
    }
}