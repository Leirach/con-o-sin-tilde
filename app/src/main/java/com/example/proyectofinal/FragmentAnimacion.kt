package com.example.proyectofinal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAnimacion.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentAnimacion : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: Int? = null

    lateinit var botonContinuar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.animacion, container, false)
        botonContinuar = rootView.findViewById<Button>(R.id.continuar)

        botonContinuar.setOnClickListener {
            if (param2 == 0) {
                val fragment = FragmentSega.newInstance("string", "test")
                val ft = childFragmentManager.beginTransaction()
                ft.replace(R.id.sega, fragment).commit()
            } else if (param2 == 1) {
                val fragment = FragmentSega.newInstance("string", "test")
                val ft = childFragmentManager.beginTransaction()
                ft.replace(R.id.contexto, fragment).commit()
            } else {
                val fragment = FragmentSega.newInstance("string", "test")
                val ft = childFragmentManager.beginTransaction()
                ft.replace(R.id.hiato, fragment).commit()

            }
        }



        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentAnimacion.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            FragmentAnimacion().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}