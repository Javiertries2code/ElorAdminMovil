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
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import com.elorrieta.alumnoclient.socketIO.LoginSocket
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.android.material.appbar.MaterialToolbar


class LoginFragment : Fragment() {
    private var name: String? = null
    private var password: String? = null
    val loginFragment: String = "loginFragment"
    private lateinit var loginSocket: LoginSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginSocket = (requireActivity() as MainActivity).getLoginSocket()


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

        /*
                finding the toolbar here, seeting it up as actionbar, for some reasons
                hacemos el setting


                 */
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarLogin)


        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Configura el logo y el título


        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowHomeEnabled(true) // Asegura que el logo se muestre
            setLogo(R.drawable.elorrietalogo) // Reemplaza con el recurso de tu logo
            setDisplayUseLogoEnabled(true) // Habilita el uso del logo
            setDisplayShowTitleEnabled(true) // 🔹 Muestra el título junto con el logo
            title = "Login"
        }


        // Populate EditText fields with data
        usernameTextView?.setText("teacher1@email.com")
        passwordTextView?.setText("123")


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
            Log.d("deliveredLoginSocket", "Recibido en dologin $usernameSent + $passwordSent")

            Log.d(
                loginFragment,
                "Botón loginRegister presionado con usuario: $usernameSent y contraseña: $passwordSent"
            )



            Log.d(loginFragment, "Se ejecutó loginSocket.doLogin")
            loginSocket.doLogin(usernameSent, passwordSent)


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
