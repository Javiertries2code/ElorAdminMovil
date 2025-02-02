package com.elorrieta.alumnoclient

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import com.elorrieta.alumnoclient.socketIO.LoginSocket
import androidx.lifecycle.lifecycleScope
import com.elorrieta.alumnoclient.room.AppDatabase
import com.elorrieta.alumnoclient.room.RoomUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameTextView = view.findViewById<EditText>(R.id.username)
        val passwordTextView = view.findViewById<EditText>(R.id.password)
        val loginButton = view.findViewById<Button>(R.id.btnloginRegister)
        val loginResetPasswrod = view.findViewById<Button>(R.id.btnloginResetPasswrod)

        val db = AppDatabase.getInstance(requireContext()) // ✅ Obtener la instancia correctamente

        var lastUser: RoomUser? = null


        lifecycleScope.launch(Dispatchers.IO) {
             lastUser = db.roomDao().getLastLoggedUser()
                     }
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarLogin)


        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)



        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setLogo(R.drawable.elorrietalogo)
            setDisplayUseLogoEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = "Login"
        }


       if (lastUser?.remember  == true){
            usernameTextView?.setText(lastUser?.email)
            passwordTextView?.setText(lastUser?.password)
        }
        else{
           usernameTextView?.setText("Usuario")
           passwordTextView?.setText("Password")
       }



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
    fun hasRemember(): Boolean {
        val checkBox = view?.findViewById<CheckBox>(R.id.CheckBoxLogin)
        return checkBox?.isChecked ?: false
    }

}
