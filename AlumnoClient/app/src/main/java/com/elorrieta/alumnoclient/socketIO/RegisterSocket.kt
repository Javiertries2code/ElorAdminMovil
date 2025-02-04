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


        }



//

    fun registerUser(teacher: Teacher) {
        Log.d(tag, "Recibido en registerUser ->teacher ->$teacher")
        val message = JSONObject().apply {
            put("email", teacher.email)
            put("name", teacher.name)
            put("lastName", teacher.lastName)
            put("address", teacher.address)
            put("phone1", teacher.phone1)
            put("phone2", teacher.phone2)
            put("dni", teacher.dni)
            put("user_type", teacher.user_type)
            put("passwordHashed", teacher.passwordHashed)
            put("passwordNotHashed", teacher.passwordNotHashed)
        }
        socket.emit(Events.ON_REGISTER_TEACHER.value, Gson().toJson(message))

        Log.d(tag, "Attempt to register- $message")
    }

    fun registerUser(student: Student) {
        Log.d(tag, "Recibido en registerUser ->student $student")
        val message = JSONObject().apply {
            put("email", student.email)
            put("name", student.name)
            put("lastName", student.lastName)
            put("address", student.address)
            put("phone1", student.phone1)
            put("phone2", student.phone2)
            put("dni", student.dni)
            put("user_type", student.user_type)
            put("passwordHashed", student.passwordHashed)
            put("passwordNotHashed", student.passwordNotHashed)
        }
        socket.emit(Events.ON_REGISTER_STUDENT.value, Gson().toJson(message))

        Log.d(tag, "Attempt to register- $message")
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
