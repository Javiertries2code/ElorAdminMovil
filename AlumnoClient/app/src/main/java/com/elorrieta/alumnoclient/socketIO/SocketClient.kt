package com.elorrieta.alumnoclient.socketIO

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
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


        socket.on(Events.ON_CHANGE_NO_MAIL.value) {args->
            val activity = activity as? MainActivity
            val toaster =
                "Se ha completado la accion de cambio de contrasena y No se envio de email"
            activity?.runOnUiThread {
                (activity as? MainActivity)?.toaster(toaster)
            }
        }

        /////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////
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
//////////////////////////////////////////////////////////////////////////
        socket.on(Events.ON_RESET_PASSWORD_SUCCESSFULL.value) {
            Log.d("mysockets", "ON_RESET_PASSWORD_SUCCESSFULL")

            val activity = activity as? MainActivity

            val toaster = "Si el usuario existe, se habra enviado un email con su nueva conrasena"
            activity?.runOnUiThread {
                (activity as? MainActivity)?.toaster(toaster)
            }
        }

        /////////////////////////////////
        socket.on(Events.ON_LOGIN_USER_NOT_FOUND_ANSWER.value) {args->
            Log.d("mysockets", "ON_LOGIN_USER_NOT_FOUND_ANSWER")
            val activity = activity as? MainActivity
            val toaster = "Usuario no encontrado o password incorrecto"
            activity?.runOnUiThread {
                (activity as? MainActivity)?.toaster(toaster)
            }
        }
/////////////////////////////////////////////

        socket.on(Events.ON_RESET_PASSWORD_FAILER.value) {args->
            Log.d("mysockets", "ON_RESET_PASSWORD_FAILER")

            val activity = activity as? MainActivity
            val toaster = "Nose ha podido realizar la operacion"
            activity?.runOnUiThread {
                (activity as? MainActivity)?.toaster(toaster)
            }
        }
        ///////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////

        //////////////////////////////////////////////
        socket.on(Events.ON_NOT_REGISTERED.value) { args ->
            Log.d("mysockets", "ON_NOT_REGISTERED")

            val response = args[0] as JSONObject
            val activity = activity as? MainActivity
            val toaster = "Debe registrarse antes de continuar"

            activity?.runOnUiThread {
                (activity as? MainActivity)?.toaster(toaster)
            }

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

            Log.d(
                "notRegisteredAnswer",
                "Datos recibidos - ID: $id, Email: $email, Tipo: $userType"
            )

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
                putSerializable("user", user)
            }
            activity?.navigate(AppFragments.REGISTER, bundle)
        }
        ////////////////////////////////////////////////////////////////
        socket.on(Events.ON_LOGIN_SUCCESS_ANSWER.value) { args ->
            Log.d("mysockets", "ON_LOGIN_SUCCESS_ANSWER")


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
                val toaster = "Usuario se ha logueado"
                activity?.runOnUiThread {
                    (activity as? MainActivity)?.toaster(toaster)
                }
                myActivity.saveRoomUser(email, passwordHashed, passwordNotHashed)
                Log.d("loginAnswer", email)
                Log.d("loginAnswer", "ON_LOGIN_SUCCESS_ANSWER")


            }
            type_user_redirect = userType;

            var user: Serializable? = null

            Log.d("mysockets", "Intenta cambiar el fragmento")
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
                    myActivity?.saveRoomUser(email, passwordHashed, passwordNotHashed)

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
                    myActivity?.saveRoomUser(email, passwordHashed, passwordNotHashed)
                    val bundle = Bundle().apply {
                        putSerializable("user", user)
                    }
                    myActivity?.navigate(AppFragments.TEACHER_PROFILE, bundle)
                }
            }
        }

        ///////////////////////////////////////////////////////
        socket.on(Events.ON_UPDATE_ANSWER_FAILURE.value) { args ->
            Log.d("sockets", "ON_UPDATE_ANSWER_FAILURE")

            val response = args[0] as JSONObject

            val activity = activity as? MainActivity
            val toaster = "No se ha podido registrar al usuario"
            activity?.runOnUiThread {
                (activity as? MainActivity)?.toaster(toaster)
            }
            //  activity?.navigate(AppFragments.LOGIN)
            Log.d(tag, "password changed, switchin to loginfragment")

        }

////////////////////////////////////////////////////////////
        socket.on(Events.ON_UPDATE_ANSWER_SUCCESS.value) { args ->
            Log.d("sockets", "ON_UPDATE_ANSWER_SUCCESS")

            val response = args[0] as JSONObject

            val activity = activity as? MainActivity
            val toaster = "El usuario se ha registrado con exito"
            activity?.runOnUiThread {
                (activity as? MainActivity)?.toaster(toaster)
            }
            activity?.navigate(AppFragments.LOGIN)
            Log.d(tag, "password changed, switchin to loginfragment")

        }
        ////////////////////////////////////////
        socket.on(Events.ON_RESET_PASSWORD_SUCCESSFULL.value) { args ->
            Log.d("mysockets", "ON_RESET_PASSWORD_SUCCESSFULL")

            val response = args[0] as JSONObject

            val activity = activity as? MainActivity

            val toaster = "El password ha cambiado"
            activity?.runOnUiThread {
                (activity as? MainActivity)?.toaster(toaster)
            }
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
