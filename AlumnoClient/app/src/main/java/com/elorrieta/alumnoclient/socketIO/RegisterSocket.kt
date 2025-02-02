package com.elorrieta.alumnoclient.socketIO

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.elorrieta.alumnoclient.AppFragments
import com.elorrieta.alumnoclient.LoginFragment
import com.elorrieta.alumnoclient.MainActivity
import com.elorrieta.alumnoclient.R
import com.elorrieta.alumnoclient.StudentProfileFragment
import com.elorrieta.alumnoclient.TeacherProfileFragment
import com.elorrieta.alumnoclient.entity.Meeting
import com.elorrieta.alumnoclient.entity.Student
import com.elorrieta.alumnoclient.entity.Teacher
import com.elorrieta.alumnoclient.session.SessionManager
import com.elorrieta.socketsio.sockets.config.Events
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.socket.client.Socket
import org.json.JSONObject
import java.io.Serializable


class RegisterSocket(private val context: Context)
{

    private val socket: Socket = SocketManager.getSocket() // Reutiliza el socket
    private val tag = "RegisterSocket"

    init {

        socket.on(Events.ON_RESET_PASSWORD_SUCCESSFULL.value) { args ->
            val response = args[0] as JSONObject

            val activity = context as? MainActivity
            activity?.toaster("El password ha cambiado")
            activity?.navigate(AppFragments.LOGIN)
            Log.d(tag, "password changed, switchin to loginfragment")


            }
        }




    fun domyStuff() {
        socket.emit(Events.ON_GET_ALL_MEETINGS.value)
        Log.d(tag, "Requesting all meetings...")
    }

    fun registerUser(teacher: Teacher) {
        Log.d(tag, "Recibido en dologin $teacher")
        val message = JSONObject().apply {
            put("teacher", teacher)
        }
        socket.emit(Events.ON_REGISTER_TEACHER.value, Gson().toJson(message))

        Log.d(tag, "Attempt  to register- $message")

    }
    fun registerUser(student: Student) {
        Log.d(tag, "Recibido en dologin $student")
        val message = JSONObject().apply {
            put("teacher", student)
        }
        socket.emit(Events.ON_REGISTER_STUDENT.value, Gson().toJson(message))

        Log.d(tag, "Attempt  to register- $message")
    }

fun resetPassword(oldPass: String, newPass: String){
    Log.d(tag, "Recibido en reset password $oldPass + $newPass")

    val activity = context as? MainActivity
    activity?.toaster("Se han enviado las contrasenas")

    val message = JSONObject().apply {
        put("oldPass", oldPass)
        put("newPass", newPass)
    }

    socket.emit(Events.ON_RESET_PASSWORD.value, Gson().toJson(message))


    // activity.findViewById<TextView>(R.id.textView).append("\nAttempt of login with credentials- $message")
    Log.d(tag, "Enviada nueva contrasena\" - $message")


}
}