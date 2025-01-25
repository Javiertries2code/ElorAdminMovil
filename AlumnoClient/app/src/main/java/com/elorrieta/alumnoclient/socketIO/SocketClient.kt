package com.elorrieta.alumnoclient.socketIO

import android.app.Activity
import android.util.Log
import android.widget.TextView
import com.elorrieta.alumnoclient.R

import com.elorrieta.alumnoclient.entity.Student
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
class SocketClient (private val activity: Activity) {

    //at home generally  192.168.1.40

    // Server IP:Port

    private val port = "4006"
    private val ip = "192.168.1.40".trim() // Porsi la lio al cortapegar :-P
    private val ipPort = "http://$ip:$port"


    private val socket: Socket = IO.socket(ipPort)

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

        socket.on(Socket.EVENT_CONNECT_ERROR) {args ->Log.e(tag, "ERROR:${args[0]}")
            Log.d(tag, "Disconnected...")
        }


        // Event called when the socket gets an answer from a login attempt.
        // We get the message and print it. Note: this event is called after
        // We try to login
        socket.on(Events.ON_LOGIN_ANSWER.value) { args ->

            // The response from the server is a JSON
            val response = args[0] as JSONObject

            // The answer should be like this:
            // {"id": 0, "name": "patata", "surname": "potato", "pass": "pass", "edad": 20}
            // We extract the field 'message'
            val message = response.getString("message") as String

            // We parse the JSON into an Student because... ¯_(ツ)_/¯
            val gson = Gson()
            val jsonObject = gson.fromJson(message, JsonObject::class.java)
            val idStudent = jsonObject["id"].asInt
            val name = jsonObject["name"].asString
            val lastName = jsonObject["surname"].asString
            val passwordNotHashed = jsonObject["password"].asInt

            val alumno = Student()
            alumno.idStudent = idStudent
            alumno.name = name
            alumno.lastName = lastName
            alumno.passwordNotHashed = passwordNotHashed

            // And... we list the Student in the list and in the Log
            activity.findViewById<TextView>(R.id.textView).append("\nAnswer to Login:$alumno")
            Log.d(tag, "Answer to Login: $alumno")
        }

        // Event called when the socket gets an answer from a getAll.
        // We get the message and print it.
        socket.on(Events.ON_GET_ALL_STUDENTS.value) { args ->

            // The response from the server is a JSON
            val response = args[0] as JSONObject


            val message = response.getString("message") as String

            // We parse the JSON. Note we use Student to parse the server response
            val gson = Gson()
            val itemType = object : TypeToken<List<Student>>() {}.type
            val list = gson.fromJson<List<Student>>(message, itemType)

            // The logging
            activity.findViewById<TextView>(R.id.textView).append("\nAnswer to getAll:$list")
            Log.d(tag, "Answer to getAll: $list")
        }
    }

    // Default events

    // This method is called when we want to establish a connection with the server
    fun connect() {
        socket.connect()

        // Log traces
        activity.findViewById<TextView>(R.id.textView).append("\n" + "Connecting to server...")
        Log.d (tag, "Connecting to server...")
    }

    // This method is called when we want to disconnect from the server
    fun disconnect() {
        socket.disconnect()

        // Log traces
        activity.findViewById<TextView>(R.id.textView).append("\n" + "Disconnecting from server...")
        Log.d (tag, "Disconnecting from server...")
    }

    // Custom events

    // This method is called when we want to login. We get the userName,
    // put in into an MessageOutput, and convert it into JSON to be sent
    fun doLogin(email: String, password: String) {

        val message = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        socket.emit(Events.ON_LOGIN.value, Gson().toJson(message))

        // Log traces
        activity.findViewById<TextView>(R.id.textView).append("\nAttempt of login with credentials- $message")
        Log.d (tag, "Attempt of login witrh credentials  - $message")
    }

    // This method is called when we want to getAll the Alumno.
    fun doGetAll() {
        socket.emit(Events.ON_GET_ALL_STUDENTS.value)

        // Log traces
        activity.findViewById<TextView>(R.id.textView).append("\nAttempt of getAll...")
        Log.d (tag, "Attempt of getAll...")
    }

    // This method is called when we want to logout. We get the userName,
    // put in into an MessageOutput, and convert it into JSON to be sent
    fun doLogout(userName: String) {
        val message = MessageInput(userName) // The server is expecting a MessageInput
        socket.emit(Events.ON_LOGOUT.value, Gson().toJson(message))

        // Log traces
        activity.findViewById<TextView>(R.id.textView).append("\nAttempt of Logout - $message")
        Log.d (tag, "Attempt of logout - $message")
    }
}
