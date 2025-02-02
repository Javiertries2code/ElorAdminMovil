package com.elorrieta.alumnoclient.socketIO

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.elorrieta.alumnoclient.LoginFragment
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


            val activity = context as? FragmentActivity

            if (activity != null) {
                activity.runOnUiThread() {
                    Toast.makeText(
                        context,
                        "Password changed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

                val newFragment: Fragment =  LoginFragment()
Log.d(tag, "password changed, switchin to loginfragment")
            if (activity != null) {
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, newFragment)
                    .addToBackStack(null)
                    .commit()
            }

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

    val myToast = context as? FragmentActivity?
    if (myToast != null) {
        myToast.runOnUiThread() {
            Toast.makeText(
                context,
                "Enviada nueva contrasena",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    val message = JSONObject().apply {
        put("oldPass", oldPass)
        put("newPass", newPass)
    }

    socket.emit(Events.ON_LOGIN.value, Gson().toJson(message))


    // activity.findViewById<TextView>(R.id.textView).append("\nAttempt of login with credentials- $message")
    Log.d(tag, "Enviada nueva contrasena\" - $message")


}
}