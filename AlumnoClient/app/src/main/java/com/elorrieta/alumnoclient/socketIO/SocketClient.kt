package com.elorrieta.alumnoclient.socketIO

import android.app.Activity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.elorrieta.alumnoclient.R
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

/**
 * The client
 */
class SocketClient(private val activity: Activity) {

    // Server IP:Port
//    private val port = "4000"
//    private val ip = "192.168.1.40".trim() // Porsi la lio al cortapegar :-P
//    private val ipPort = "http://$ip:$port"

    private val socket: Socket = SocketManager.getSocket()

    // For log purposes
    private var tag = "socket.io"

    // We add here ALL the events we are eager to LISTEN from the server
    init {
        // Event called when the socket connects
        socket.on(Socket.EVENT_CONNECT) {
            Log.d(tag, "Connected...")
        }

        // Event called when the socket disconnects
        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d(tag, "Disconnected...")
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e(tag, "ERROR:${args[0]}")
            Log.d(tag, "Disconnected...")
        }

        socket.on(Events.ON_NOT_REGISTERED.value) { args ->
            val response = args[0] as JSONObject
            Log.d("logNotRegistered", "In not registered event")

            val message = response.getString("message")
            val gson = Gson()
            val jsonObject = gson.fromJson(message, JsonObject::class.java)

            val id = jsonObject["id"].asInt
            val email = jsonObject["email"].asString
            val passwordNotHashed = jsonObject["passwordNotHashed"].asInt
            val passwordHashed = jsonObject["passwordHashed"].asString
            val userType = jsonObject["user_type"].asString
            val name = jsonObject["name"].asString

            var user: Any? = null
            activity.runOnUiThread {
                Toast.makeText(activity, "Bienvenido $email \n, debe registrarse", Toast.LENGTH_SHORT).show()
            }

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
                    Log.e("logNotRegistered", "I assume it exists in DB")
                }
            }

            activity.findViewById<TextView>(R.id.textView).append("\nNot Registered: $user")
            Log.d("logNotRegistered", "Not Registered: $user")
            Log.d("logNotRegistered", "Event received with message: ${user.toString()}")
        }

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

            activity.runOnUiThread {
                Toast.makeText(activity, "Login correcto \n Bienvenido ${alumno.name}", Toast.LENGTH_SHORT).show()
            }

            activity.findViewById<TextView>(R.id.textView).append("\nAnswer to Login:$alumno")
            Log.d(tag, "Answer to Login: $alumno")
        }

    }

    // Default events
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

    // Custom events
    fun doLogin(email: String, password: String) {
        val message = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        socket.emit(Events.ON_LOGIN.value, Gson().toJson(message))
        activity.findViewById<TextView>(R.id.textView).append("\nAttempt of login with credentials- $message")
        Log.d(tag, "Attempt of login with credentials - $message")
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

    fun resetPassword(email: String) {
        val message = JSONObject().apply {
            put("email", email)
        }

        socket.emit(Events.ON_RESET_PASSWORD.value, Gson().toJson(message))
        activity.findViewById<TextView>(R.id.textView).append("\nAttempt of reset password with email- $message")
        Log.d(tag, "Attempt of reset password with email - $message")
    }

}
