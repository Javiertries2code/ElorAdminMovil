package com.elorrieta.alumnoclient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.elorrieta.alumnoclient.socketIO.LoginSocket
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private var name: String? = null
    private var password: String? = null
    val loginFragment: String = "loginFragment"
    private lateinit var loginSocket: LoginSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(/* key = */ "email")
            password = it.getString(/* key = */ "password")
            loginSocket = (requireActivity() as MainActivity).getLoginSocket()

            Log.d(loginFragment, "Name: ${name.orEmpty()}")
            Log.d(loginFragment, "Password: ${password.orEmpty()}")


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameTextView = view.findViewById<EditText>(R.id.username)
        val passwordTextView = view.findViewById<EditText>(R.id.password)
        val loginButton = view.findViewById<Button>(R.id.btnloginRegister)
        val loginResetPasswrod = view.findViewById<Button>(R.id.btnloginResetPasswrod)


        // Populate EditText fields with data
        usernameTextView?.setText(name.orEmpty())
        passwordTextView?.setText(password.orEmpty())


//As my fragment comes  straight from main, that is a appcompact activity... it should works
        //val loginSocket = (requireActivity() as MainActivity).getLoginSocket() // Acceder a la instancia

        if (loginButton == null) {
            Log.e(loginFragment, "El botón btnloginRegister no se encontró en el layout.")
            return
        } else {
            Log.d(loginFragment, "El btnloginRegister encontrado .")

        }

        loginButton.setOnClickListener {
            val usernameSent = usernameTextView.text.toString()
            val passwordSent = passwordTextView.text.toString()

            Log.d(loginFragment, "Botón loginRegister presionado con usuario: $usernameSent y contraseña: $passwordSent")



            Log.d(loginFragment, "Se ejecutó loginSocket.doLogin")
            loginSocket.doLogin(usernameSent, passwordSent)

            lifecycleScope.launch {
                delay(3000) // Espera 3 segundos sin bloquear la UI

            }
        }

        loginResetPasswrod.setOnClickListener {
            val usernameSent = usernameTextView.text.toString()
            Log.d(loginFragment, "it clicked call resetPassword, deliverin $usernameSent")
//look the notes on exrtension functions, this is just checking the null before,
// //as there was a mismatch Sting String?
        loginSocket.resetPassword(usernameSent)

            Log.d(loginFragment, "Des it even return from the call, it should change frangment")

            lifecycleScope.launch {
                delay(3000) // Espera 3 segundos sin bloquear la UI

            }
        }


    }

//    companion object {
//        const val NAME_BUNDLE = "name"
//        const val PASSWORD_BUNDLE = "password"
//
//        @JvmStatic
//        fun newInstance(name: String, password: String) =
//            LoginFragment().apply {
//                arguments = Bundle().apply {
//                    putString(NAME_BUNDLE, name)
//                    putString(PASSWORD_BUNDLE, password)
//                }
//            }
//    }
}
