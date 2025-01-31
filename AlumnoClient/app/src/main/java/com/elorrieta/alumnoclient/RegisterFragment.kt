package com.elorrieta.alumnoclient

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.elorrieta.alumnoclient.entity.Student
import com.elorrieta.alumnoclient.entity.Teacher
import com.elorrieta.alumnoclient.session.SessionManager
import com.elorrieta.alumnoclient.socketIO.RegisterSocket

private lateinit var thisSocket: RegisterSocket

private lateinit var nameEditText: EditText
private lateinit var surnameEditText: EditText
private lateinit var emailEditText: EditText
private lateinit var phone1EditText: EditText
private lateinit var phone2EditText: EditText
private lateinit var passwordEditText: EditText
private lateinit var registerCiclos: TextView
private lateinit var registerButton: Button
private lateinit var registerFoto: Button

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

        nameEditText = view.findViewById(R.id.registerName)
        surnameEditText = view.findViewById(R.id.registerSurname)
        emailEditText = view.findViewById(R.id.registerEmail)
        phone1EditText = view.findViewById(R.id.registerPhone1)
        phone2EditText = view.findViewById(R.id.registerPhone2)
        passwordEditText = view.findViewById(R.id.registerNewPassword)
        registerButton = view.findViewById(R.id.registerSubmit)
        registerCiclos = view.findViewById(R.id.registerCiclos)

        val user = arguments?.getSerializable("user")

        when (user) {
            is Student -> {
                registerCiclos.text = "Mis ciclos"
                registerCiclos.visibility = View.VISIBLE
                Log.d(tag, "Student recibido: ${user.name}")
                nameEditText.setText(user.name ?: "Sin datos recibidos")
                surnameEditText.setText(user.lastName ?: "Sin datos recibidos")
                emailEditText.setText(user.email ?: "Sin datos recibidos")
                phone1EditText.setText(user.phone1 ?: "Sin datos recibidos")
                phone2EditText.setText(user.phone2 ?: "Sin datos recibidos")
            }
            is Teacher -> {
                registerCiclos.visibility = View.GONE
                Log.d(tag, "Teacher recibido: ${user.name}")
                nameEditText.setText(user.name ?: "Sin datos recibidos")
                surnameEditText.setText(user.lastName ?: "Sin datos recibidos")
                emailEditText.setText(user.email ?: "Sin datos recibidos")
                phone1EditText.setText(user.phone1 ?: "Sin datos recibidos")
                phone2EditText.setText(user.phone2 ?: "Sin datos recibidos")
            }
            else -> {
                Log.e(tag, "No se recibió ningún usuario válido")
                registerCiclos.visibility = View.GONE
            }
        }


//            Log.d(tag, "Usuario recibido: ${user.name}")
//            nameEditText.setText(user.name ?: "Sin datos recibidos")
//            surnameEditText.setText(user.lastName ?: "Sin datos recibidos")
//            emailEditText.setText(user.email ?: "Sin datos recibidos")
//            phone1EditText.setText(user.phone1 ?: "Sin datos recibidos")
//            phone2EditText.setText(user.phone2 ?: "Sin datos recibidos")


        registerButton.setOnClickListener {
            //it seems i gotta choose type of student or teacher here
           val newUser =  SessionManager.getUser()
            when (newUser) {
                is Student -> {
                    newUser?.let{ it->
                        it.name = nameEditText.text.toString()
                        it.lastName = surnameEditText.text.toString()
                        it.email = emailEditText.text.toString()
                      it.phone1 = phone1EditText.text.toString()
                        it.phone2 = phone2EditText.text.toString()
                      //  it.passwordNotHashed = passwordEditText.text.toString()

                    }
                }
                is Teacher -> {
                    newUser?.let{ it->
                        it.name = nameEditText.text.toString()
                        it.lastName = surnameEditText.text.toString()
                        it.email = emailEditText.text.toString()
                        it.phone1 = phone1EditText.text.toString()
                        it.phone2 = phone2EditText.text.toString()
                      //  it.passwordNotHashed = passwordEditText.text.toString()

                    }
                    thisSocket.registerUser()//it should take it from session manager

                }
//                when (newUser) {
//                    is Student -> thisSocket.registerUser(newUser)
//                    is Teacher -> thisSocket.registerUser(newUser)
//                    else -> Log.e(tag, "El usuario no es válido para el registro")
//                }
            }
        }
    }
}
