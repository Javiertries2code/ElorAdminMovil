package com.elorrieta.alumnoclient.socketIO

import android.app.Activity
import kotlinx.coroutines.withContext

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.elorrieta.alumnoclient.LoginFragment
import com.elorrieta.alumnoclient.MainActivity
import com.elorrieta.alumnoclient.R
import com.elorrieta.alumnoclient.RegisterFragment
import com.elorrieta.alumnoclient.StudentProfileFragment
import com.elorrieta.alumnoclient.TeacherProfileFragment
import com.elorrieta.alumnoclient.entity.Student
import com.elorrieta.alumnoclient.entity.Teacher
import com.elorrieta.alumnoclient.room.RoomUser
import com.elorrieta.alumnoclient.session.SessionManager
import com.elorrieta.alumnoclient.socketIO.model.MessageInput
import com.elorrieta.socketsio.sockets.config.Events
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.Serializable

//Parece que el puto casque era por usar ACtivity, en vez de AppCompatActivity
//en el puto contructor
class LoginSocket(private val context: Context) {

    private val socket: Socket = SocketManager.getSocket() // Reutiliza el socket
    private val tag = "loginSocket"
    private val deliveredLoginSocket = "deliveredLoginSocket"
    private val loginAnswer = "loginAnswer"

    private val notRegisteredAnswer = "notRegisteredAnswer"
    private var type_user_redirect: String = "";


    init {

        socket.on(Events.ON_NOT_REGISTERED.value) { args ->
            val response = args[0] as JSONObject

            val myToast = context as? FragmentActivity?
            if (myToast != null) {
                myToast.runOnUiThread() {
                    Toast.makeText(
                        context,
                        "Debe registrarse",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Log.d(notRegisteredAnswer, "Coming into socket.on(Events.ON_NOT_REGISTERED.value)")
            Log.d(notRegisteredAnswer, "Tipo de contexto recibido: ${context::class.java.name}")
            Log.d(notRegisteredAnswer, "Tipo de contexto recibido: ${context::class.java.name}")
            Log.d(notRegisteredAnswer, "¿Es una AppCompatActivity? ${context is AppCompatActivity}")
            Log.d(notRegisteredAnswer, "¿Es una FragmentActivity? ${context is FragmentActivity}")
            val message = response.getString("message")
            val gson = Gson()
            val jsonObject = gson.fromJson(message, JsonObject::class.java)

            val id = jsonObject["id"].asInt
            val email = jsonObject["email"].asString
            val passwordNotHashed = jsonObject["passwordNotHashed"].asInt
            val passwordHashed = jsonObject["passwordHashed"].asString
            val userType = jsonObject["user_type"].asString
            val name = jsonObject["name"].asString

            var user: Serializable? = null

            when (userType) {
                "student" -> {
                    val student = Student().apply {
                        idStudent = id
                        this.name = name
                        this.email = email
                        this.passwordNotHashed = passwordNotHashed
                        this.passwordHashed = passwordHashed
                    }
                    SessionManager.setUser(student)
                    user = student
                }

                "teacher" -> {
                    val teacher = Teacher().apply {
                        idTeacher = id
                        this.name = name
                        this.email = email
                        this.passwordNotHashed = passwordNotHashed
                        this.passwordHashed = passwordHashed
                    }
                    SessionManager.setUser(teacher)
                    user = teacher
                }

                else -> {
                    Log.e(notRegisteredAnswer, "I assume it exists in DB nas is registered")
                    return@on
                }
            }

            Log.d(notRegisteredAnswer, "Object user loaded = : $user")
            Log.d(notRegisteredAnswer, "Event received with message: ${user.toString()}")

            if (user == null) {
                Log.e(
                    notRegisteredAnswer,
                    "El usuario es null, no se puede pasar a RegisterFragment"
                )
                return@on
            }

            // Asegurar que el contexto es una FragmentActivity antes de proceder
            val activity = context as? FragmentActivity ?: run {
                Log.e(
                    notRegisteredAnswer,
                    "El contexto no es una FragmentActivity, no se puede cambiar el fragmento"
                )
                return@on
            }

            if (activity.supportFragmentManager.isStateSaved) {
                Log.e(notRegisteredAnswer, "El estado del fragmento no es válido para reemplazarlo")
                return@on
            }

            Log.d(notRegisteredAnswer, "Intenta cambiar el fragmento")

            activity.runOnUiThread {
                val bundle = Bundle().apply {
                    putSerializable("user", user)
                }
                val registerFragment = RegisterFragment().apply {
                    arguments = bundle
                }

                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, registerFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        socket.on(Events.ON_LOGIN_SUCCESS_ANSWER.value) { args ->
            val response = args[0] as JSONObject
            Log.d(notRegisteredAnswer, "Coming into socket.on(Events.ON_NOT_REGISTERED.value)")
            Log.e(notRegisteredAnswer, "Tipo de contexto recibido: ${context::class.java.name}")
            Log.e(notRegisteredAnswer, "Tipo de contexto recibido: ${context::class.java.name}")
            Log.e(notRegisteredAnswer, "¿Es una AppCompatActivity? ${context is AppCompatActivity}")
            Log.e(notRegisteredAnswer, "¿Es una FragmentActivity? ${context is FragmentActivity}")
            val message = response.getString("message")
            val gson = Gson()
            val jsonObject = gson.fromJson(message, JsonObject::class.java)

            val id = jsonObject["id"].asInt
            val email = jsonObject["email"].asString
            val passwordNotHashed = jsonObject["passwordNotHashed"].asInt
            val passwordHashed = jsonObject["passwordHashed"].asString
            val userType = jsonObject["user_type"].asString
            val name = jsonObject["name"].asString

//To access the function and saving room user
            val myActivity = context as? MainActivity

            if (myActivity != null) {
                myActivity.saveRoomUser(email, passwordHashed, passwordNotHashed)
            }


//some day i will finally do the toastr function... lets keep waiting :-P


            val acitvity = context as? FragmentActivity?

            if (acitvity != null) {
                acitvity.runOnUiThread() {
                    Toast.makeText(
                        context,
                        "Usuario logueado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }



            type_user_redirect = userType;

            var user: Serializable? = null

            // Elegir el tipo de usuario
            when (userType) {
                "student" -> {
                    val student = Student().apply {
                        idStudent = id
                        this.name = name
                        this.email = email
                        this.passwordNotHashed = passwordNotHashed
                        this.passwordHashed = passwordHashed
                    }
                    SessionManager.setUser(student)
                    user = student
                }

                "teacher" -> {
                    val teacher = Teacher().apply {
                        idTeacher = id
                        this.name = name
                        this.email = email
                        this.passwordNotHashed = passwordNotHashed
                        this.passwordHashed = passwordHashed
                    }
                    SessionManager.setUser(teacher)
                    user = teacher
                }

                else -> {
                    Log.e(loginAnswer, "I assume it exists in DB nas is registered")
                    return@on
                }
            }

            Log.d(notRegisteredAnswer, "Object user loaded = : $user")
            Log.d(notRegisteredAnswer, "Event received with message: ${user.toString()}")

            if (user == null) {
                Log.e(loginAnswer, "El usuario es null, no se puede pasar a RegisterFragment")
                return@on
            }

            // Asegurar que el contexto es una FragmentActivity antes de proceder
            val activity = context as? FragmentActivity ?: run {
                Log.e(
                    loginAnswer,
                    "El contexto no es una FragmentActivity, no se puede cambiar el fragmento"
                )
                return@on
            }

            if (activity.supportFragmentManager.isStateSaved) {
                Log.e(loginAnswer, "El estado del fragmento no es válido para reemplazarlo")
                return@on
            }

            Log.d(loginAnswer, "Intenta cambiar el fragmento")

            activity.runOnUiThread {
                val bundle = Bundle().apply {
                    putSerializable("user", user)
                }

                //testing
                Log.d(loginAnswer, "type of user" + type_user_redirect)

                val newFragment: Fragment = when (type_user_redirect) {
                    "student" -> StudentProfileFragment().apply {
                        arguments = bundle
                    }

                    "teacher" -> TeacherProfileFragment().apply {
                        arguments = bundle
                    }

                    else -> throw IllegalArgumentException("Tipo de usuario desconocido: $type_user_redirect")
                }


                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, newFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        ///End init
    }

    fun doLogin(email: String, password: String) {
        Log.d(tag, "Recibido en dologin $email + $password")

        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            val activity = context as? FragmentActivity?
            if (activity != null) {
                activity.runOnUiThread() {
                    Toast.makeText(
                        context,
                        "El email o el password estan vacios",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            return
        }
        val message = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        socket.emit(Events.ON_LOGIN.value, Gson().toJson(message))


        // activity.findViewById<TextView>(R.id.textView).append("\nAttempt of login with credentials- $message")
        Log.d(tag, "Attempt of login with credentials - $message")
    }

    fun doLogout() {
        val userName = SessionManager.getEmail()
        val message = MessageInput(userName)

        //This logout, is from the local sessoin, putting it to null oji

        SessionManager.logout()

        socket.emit(Events.ON_LOGOUT.value, Gson().toJson(message))
        // activity.findViewById<TextView>(R.id.textView).append("\nAttempt of Logout - $message")
        Log.d(tag, "Attempt of logout - $message")
    }

    fun resetPassword(email: String) {
        val message = JSONObject().apply {
            put("email", email)
        }

        socket.emit(Events.ON_RESET_PASSWORD.value, Gson().toJson(message))

        val activity = context as? FragmentActivity ?: run {
            Log.e(
                notRegisteredAnswer,
                "El contexto no es una FragmentActivity, no se puede cambiar el fragmento"
            )
            return
        }

        if (activity.supportFragmentManager.isStateSaved) {
            Log.e(notRegisteredAnswer, "El estado del fragmento no es válido para reemplazarlo")
            return
        }

        Log.d(notRegisteredAnswer, "Intenta cambiar el fragmento")

        activity.runOnUiThread {
            val newFragment: Fragment = LoginFragment()



            activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, newFragment)
                .addToBackStack(null)
                .commit()
        }
        //  activity.findViewById<TextView>(R.id.textView).append("\nAttempt of reset password with email- $message")
        Log.d(tag, "Attempt of reset password with email - $message")
    }


}
