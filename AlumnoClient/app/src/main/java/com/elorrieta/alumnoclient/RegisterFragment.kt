package com.elorrieta.alumnoclient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.elorrieta.alumnoclient.entity.Student
import com.elorrieta.alumnoclient.entity.Teacher
import com.elorrieta.alumnoclient.socketIO.RegisterSocket

private lateinit var thisSocket: RegisterSocket

private val tag = "RegisterSocket"

class RegisterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisSocket = (requireActivity() as MainActivity).getRegisterSocket()
        Log.d(tag, "got into Register fragment activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getSerializable("user")

        when (user) {
            is Student -> {
                Log.d(tag, "Estudiante recibido: ${user.name}")
                view.findViewById<EditText>(R.id.registerName).setText("Hola, ${user.name}")
            }
            is Teacher -> {
                Log.d(tag, "Profesor recibido: ${user.name}")
                view.findViewById<EditText>(R.id.registerName).setText("Hola, ${user.name}")
            }
            else -> {
                Log.e(tag, "No se recibió ningún usuario válido")
            }
        }
    }
}
