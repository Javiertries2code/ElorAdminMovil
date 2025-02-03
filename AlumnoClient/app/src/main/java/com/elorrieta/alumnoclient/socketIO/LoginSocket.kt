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
import com.elorrieta.alumnoclient.AppFragments
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
       /*
        socket.on(Events.ON_LOGIN_USER_NOT_FOUND_ANSWER.value){
            val activity = context as? MainActivity
            activity?.toaster("Usuario no encontrado en la DB del centro")

        }
*/

        socket.on(Events.ON_NOT_REGISTERED.value) { args ->
            val response = args[0] as JSONObject
            val activity = context as? MainActivity
            activity?.toaster("Debe registrarse antes de continuar")

            val message = response.getString("message")
            val gson = Gson()
            val jsonObject = gson.fromJson(message, JsonObject::class.java)

            val id = jsonObject.get("id")?.asInt ?: -1
            val email = jsonObject.get("email")?.asString ?: "Sin Datos"
            val passwordNotHashed = jsonObject.get("passwordNotHashed")?.asInt ?: 0
            val passwordHashed = jsonObject.get("passwordHashed")?.asString ?: "Sin Datos"
            val userType = jsonObject.get("user_type")?.asString ?: "Sin Datos"
            val name = jsonObject.get("name")?.asString ?: "Sin Datos"

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

            }
            Log.d(notRegisteredAnswer, "Event received with message: ${user.toString()}")

            Log.d(notRegisteredAnswer, "Intenta cambiar al fragmento register")
                val bundle = Bundle().apply {
                    putSerializable("user", user)}
                activity?.navigate(AppFragments.REGISTER, bundle)
            }
/////////////////////////////////////////////////////////////NEXT
        /////////////////////////////////////////////////////////////NEXT
        /////////////////////////////////////////////////////////////NEXT
/*
        socket.on(Events.ON_LOGIN_SUCCESS_ANSWER.value) { args ->
            val response = args[0] as JSONObject

            val message = response.getString("message")
            val gson = Gson()
            val jsonObject = gson.fromJson(message, JsonObject::class.java)

            val id = jsonObject["id"].asInt
            val email = jsonObject["email"].asString
            val passwordNotHashed = jsonObject["passwordNotHashed"].asInt
            val passwordHashed = jsonObject["passwordHashed"].asString
            val userType = jsonObject["user_type"].asString
            val name = jsonObject["name"].asString

            val myActivity = context as? MainActivity
            val roomtag = "roomSocket"
            Log.d(roomtag, email)

            if (myActivity != null) {
                myActivity.toaster("Usuario se ha logueado")
                myActivity.saveRoomUser(email, passwordHashed, passwordNotHashed)
                Log.d(loginAnswer, email)
                Log.d(loginAnswer, "ON_LOGIN_SUCCESS_ANSWER")


            }
            type_user_redirect = userType;

            var user: Serializable? = null

            Log.d(loginAnswer, "Intenta cambiar el fragmento")
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
                    val bundle = Bundle().apply {
                        putSerializable("user", user)
                    }
                    myActivity?.navigate(AppFragments.STUDENT_PROFILE, bundle)
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
                    val bundle = Bundle().apply {
                        putSerializable("user", user)
                    }
                    myActivity?.navigate(AppFragments.TEACHER_PROFILE, bundle)
                }
            }
        }
        *\
 */
    }


    fun doLogin(email: String, password: String) {
        Log.d(tag, "Recibido en dologin $email + $password")

        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            val activity = context as? MainActivity
            activity?.toaster("El email o el password estan vacios")
            return
        }
        val message = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        socket.emit(Events.ON_LOGIN.value, Gson().toJson(message))
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

        val activity = context as? MainActivity?
        activity?.toaster("Se ha enviado la solicitud de recuperacion de contrasena")


        activity?.navigate(AppFragments.LOGIN)

        //  activity.findViewById<TextView>(R.id.textView).append("\nAttempt of reset password with email- $message")
        Log.d(tag, "Attempt of reset password with email - $message")
    }


}
