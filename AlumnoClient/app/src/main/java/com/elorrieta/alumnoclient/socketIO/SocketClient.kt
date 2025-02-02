package com.elorrieta.alumnoclient.socketIO

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
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

/**
 * The client
 */
class SocketClient(private val activity: AppCompatActivity) {

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

    // Custom events/*
//    fun doLogin(email: String, password: String) {
//        val message = JSONObject().apply {
//            put("email", email)
//            put("password", password)
//        }
//
//        socket.emit(Events.ON_LOGIN.value, Gson().toJson(message))
//        activity.findViewById<TextView>(R.id.textView).append("\nAttempt of login with credentials- $message")
//        Log.d(tag, "Attempt of login with credentials - $message")
//    }


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
/*
    fun resetPassword(email: String) {
        val message = JSONObject().apply {
            put("email", email)
        }

        socket.emit(Events.ON_RESET_PASSWORD.value, Gson().toJson(message))
        activity.findViewById<TextView>(R.id.textView).append("\nAttempt of reset password with email- $message")
        Log.d(tag, "Attempt of reset password with email - $message")
    }
*/
}
