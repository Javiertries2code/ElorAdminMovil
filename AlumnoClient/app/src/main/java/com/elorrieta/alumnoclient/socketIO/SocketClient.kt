package com.elorrieta.alumnoclient.socketIO

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.elorrieta.alumnoclient.AppFragments
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
import com.google.gson.reflect.TypeToken
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.io.Serializable
private var type_user_redirect: String = "";


class SocketClient(private val activity: AppCompatActivity) {

    private val socket: Socket = SocketManager.getSocket()
    private var tag = "socket.io"

    init {
        socket.on(Socket.EVENT_CONNECT) {
            Log.d(tag, "Connected...")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d(tag, "Disconnected...")
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e(tag, "ERROR:${args[0]}")
            Log.d(tag, "Disconnected...")
        }

        socket.on(Events.ON_LOGIN_USER_NOT_FOUND_ANSWER.value) {
            val activity = activity as? MainActivity
            activity?.toaster("Usuario no encontrado en la DB del centro")
        }

        ///////////////////////////////////////////////////////////////////////////
        socket.on(Events.ON_LOGIN_SUCCESS_ANSWER.value) { args ->
            val response = args[0] as JSONObject

            val message = response.getString("message")
            val gson = Gson()
            val jsonObject = gson.fromJson(message, JsonObject::class.java)

            val id = jsonObject["id"].asInt
            val email = jsonObject["email"].asString
            val lastName = jsonObject["lastName"].asString
            val phone1 = jsonObject["phone1"].asString
            val phone2 = jsonObject["phone2"].asString
            val dni = jsonObject["dni"].asString
            val address = jsonObject["address"].asString
            val passwordNotHashed = jsonObject["passwordNotHashed"].asInt
            val passwordHashed = jsonObject["passwordHashed"].asString
            val userType = jsonObject["user_type"].asString
            val name = jsonObject["name"].asString

            val myActivity = activity as? MainActivity
            val roomtag = "roomSocket"
            Log.d("ON_LOGIN_SUCCESS_ANSWER", "Datos recibidos - ID: $id, Email: $email, Tipo: $userType")

            Log.d(roomtag, email)

            if (myActivity != null) {
                myActivity.toaster("Usuario se ha logueado")
                myActivity.saveRoomUser(email, passwordHashed, passwordNotHashed)
                Log.d("loginAnswer", email)
                Log.d("loginAnswer", "ON_LOGIN_SUCCESS_ANSWER")


            }
            type_user_redirect = userType;

            var user: Serializable? = null

            Log.d("loginAnswer", "Intenta cambiar el fragmento")
            // Elegir el tipo de usuario
            when (userType) {
                "student" -> {
                    val student = Student().apply {
                        idStudent = id
                        this.name = name
                        this.email = email
                        this.lastName = lastName
                        this.phone1 = phone1
                        this.phone2 = phone2
                        this.dni = dni
                        this.address = address
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
                        this.lastName = lastName
                        this.phone1 = phone1
                        this.phone2 = phone2
                        this.dni = dni
                        this.address = address
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

        //////////////////////////////////////////////
        socket.on(Events.ON_NOT_REGISTERED.value) { args ->
            val response = args[0] as JSONObject
            val activity = activity as? MainActivity
            activity?.toaster("Debe registrarse antes de continuar")

            val message = response.getString("message")
            val gson = Gson()
            val jsonObject = gson.fromJson(message, JsonObject::class.java)

            val id = jsonObject["id"].asInt
            val email = jsonObject["email"].asString
            val lastName = jsonObject["lastName"].asString
            val phone1 = jsonObject["phone1"].asString
            val phone2 = jsonObject["phone2"].asString
            val dni = jsonObject["dni"].asString
            val address = jsonObject["address"].asString
            val passwordNotHashed = jsonObject["passwordNotHashed"].asInt
            val passwordHashed = jsonObject["passwordHashed"].asString
            val userType = jsonObject["user_type"].asString
            val name = jsonObject["name"].asString

            var user: Serializable? = null

            Log.d("notRegisteredAnswer", "Datos recibidos - ID: $id, Email: $email, Tipo: $userType")

            when (userType) {
                "student" -> {
                    val student = Student().apply {
                        idStudent = id
                        this.name = name
                        this.email = email
                        this.lastName = lastName
                        this.phone1 = phone1
                        this.phone2 = phone2
                        this.dni = dni
                        this.address = address
                        this.passwordNotHashed = passwordNotHashed
                        this.passwordHashed = passwordHashed
                    }
                    Log.d("notRegisteredAnswer", "Student created line 163: $student")

                    SessionManager.setUser(student)
                    user = student
                }
                "teacher" -> {
                    val teacher = Teacher().apply {
                        idTeacher = id
                        this.name = name
                        this.email = email
                        this.lastName = lastName
                        this.phone1 = phone1
                        this.phone2 = phone2
                        this.dni = dni
                        this.address = address
                        this.passwordNotHashed = passwordNotHashed
                        this.passwordHashed = passwordHashed
                    }
                    Log.d("notRegisteredAnswer", "Teacher created line 163: $teacher")

                    SessionManager.setUser(teacher)
                    user = teacher
                }

            }
            if (user == null) {
                Log.e("notRegisteredAnswer", "Error: Usuario in null line 171")
                return@on
            }
            Log.d("c", "Event received with message: ${user.toString()}")

            Log.d("notRegisteredAnswer", "Intenta cambiar al fragmento register")
            val bundle = Bundle().apply {
                putSerializable("user", user)}
            activity?.navigate(AppFragments.REGISTER, bundle)
        }
        ////////////////////////////////////////////////////////////////
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

            val myActivity = activity as? MainActivity
            val roomtag = "roomSocket"
            Log.d(roomtag, email)

            if (myActivity != null) {
                myActivity.toaster("Usuario se ha logueado")
                myActivity.saveRoomUser(email, passwordHashed, passwordNotHashed)
                Log.d("loginAnswer", email)
                Log.d("loginAnswer", "ON_LOGIN_SUCCESS_ANSWER")


            }
            type_user_redirect = userType;

            var user: Serializable? = null

            Log.d("loginAnswer", "Intenta cambiar el fragmento")
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

////////////////////////////////////////////////////////////
        socket.on(Events.ON_RESET_PASSWORD_SUCCESSFULL.value) { args ->
            val activity = activity as? MainActivity
            activity?.toaster("Se ha restablecido la contraseña correctamente")
        }
        ////////////////////////////////////////
        socket.on(Events.ON_RESET_PASSWORD_SUCCESSFULL.value) { args ->
            val response = args[0] as JSONObject

            val activity = activity as? MainActivity
            activity?.toaster("El password ha cambiado")
            activity?.navigate(AppFragments.LOGIN)
            Log.d(tag, "password changed, switchin to loginfragment")


        }
     /////////////////////////////////////////////////
    }

    fun connect() {
        socket.connect()
        activity.findViewById<TextView>(R.id.textView).append("\nConnecting to server...")
        Log.d(tag, "Connecting to server...")
    }

    fun disconnect() {
        socket.disconnect()
        activity.findViewById<TextView>(R.id.textView).append("\nDisconnecting from server...")
        Log.d(tag, "Disconnecting from server...")
    }

    fun doGetAll() {
        socket.emit(Events.ON_GET_ALL_STUDENTS.value)
        activity.findViewById<TextView>(R.id.textView).append("\nAttempt of getAll...")
        Log.d(tag, "Attempt of getAll...")
    }

    fun doLogout(userName: String) {
        val message = MessageInput(userName)
        socket.emit(Events.ON_LOGOUT.value, Gson().toJson(message))
        activity.findViewById<TextView>(R.id.textView).append("\nAttempt of Logout - $message")
        Log.d(tag, "Attempt of logout - $message")
    }
}
