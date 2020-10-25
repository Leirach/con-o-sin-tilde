package com.example.proyectofinal

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class ActivityJuegos : AppCompatActivity() {

    companion object {
        const val TEXT_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juegos)

        var number: Int = intent.getIntExtra("com.example.ActivityJuegos.extra.MESSAGE", 0)
        empiezaFragmentAnimacion(number)


    }

    fun empiezaFragmentAnimacion(number: Int){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = FragmentAnimacion.newInstance("test1", number)
        fragmentTransaction.replace(R.id.animacion, fragment).addToBackStack(null).commit()

    }

}