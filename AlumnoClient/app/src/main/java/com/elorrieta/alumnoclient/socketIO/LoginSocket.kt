package com.elorrieta.alumnoclient.socketIO

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.elorrieta.alumnoclient.LoginFragment
import com.elorrieta.alumnoclient.MainActivity
import com.elorrieta.alumnoclient.R
import com.elorrieta.alumnoclient.RegisterFragment
import com.elorrieta.alumnoclient.entity.Student
import com.elorrieta.alumnoclient.entity.Teacher
import com.elorrieta.alumnoclient.session.SessionManager
import com.elorrieta.alumnoclient.socketIO.model.MessageInput
import com.elorrieta.socketsio.sockets.config.Events
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.socket.client.Socket
import org.json.JSONObject
import java.io.Serializable

//Parece que el puto casque era por usar ACtivity, en vez de AppCompatActivity
//en el puto contructor
class LoginSocket(private val context: Context) {

    private val socket: Socket = SocketManager.getSocket() // Reutiliza el socket
    private val tag = "loginSocket"
    private val notRegisteredAnswer = "notRegisteredAnswer"


    init {

        socket.on(Events.ON_NOT_REGISTERED.value) { args ->
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
                    Log.e(notRegisteredAnswer, "I assume it exists in DB nas is registered")
                    return@on
                }
            }

            Log.d(notRegisteredAnswer, "Object user loaded = : $user")
            Log.d(notRegisteredAnswer, "Event received with message: ${user.toString()}")

            if (user == null) {
                Log.e(notRegisteredAnswer, "El usuario es null, no se puede pasar a RegisterFragment")
                return@on
            }

            // Asegurar que el contexto es una FragmentActivity antes de proceder
            val activity = context as? FragmentActivity ?: run {
                Log.e(notRegisteredAnswer, "El contexto no es una FragmentActivity, no se puede cambiar el fragmento")
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
        //LoginAnswer
        socket.on(Events.ON_LOGIN_ANSWER.value) { args ->
            val response = args[0] as JSONObject
            val message = response.getString("message") as String

            val gson = Gson()
            val jsonObject = gson.fromJson(message, JsonObject::class.java)
            val idStudent = jsonObject["id"].asInt
            val name = jsonObject["name"].asString
            val email = jsonObject["email"].asString
            val lastName = jsonObject["lastName"].asString
            val passwordNotHashed = jsonObject["passwordNotHashed"].asInt
            val passwordHashed = jsonObject["passwordHashed"].asString

            val alumno = Student().apply {
                this.idStudent = idStudent
                this.name = name
                this.email = email
                this.lastName = lastName
                this.passwordNotHashed = passwordNotHashed
                this.passwordHashed = passwordHashed
            }

            SessionManager.setUser(alumno)

//            activity.runOnUiThread {
//                Toast.makeText(activity, "Login correcto \n Bienvenido ${alumno.name}", Toast.LENGTH_SHORT).show()
//            }

            //  activity.findViewById<TextView>(R.id.textView).append("\nAnswer to Login:$alumno")
            Log.d(tag, "Answer to Login: $alumno")
        }

        ///End init
    }

    fun doLogin(email: String, password: String) {
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
        //  activity.findViewById<TextView>(R.id.textView).append("\nAttempt of reset password with email- $message")
        Log.d(tag, "Attempt of reset password with email - $message")
    }


}
