package com.elorrieta.alumnoclient.socketIO

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.elorrieta.alumnoclient.R
import com.elorrieta.alumnoclient.entity.Meeting
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



class TeacherSocket(private val context: Context) {

    private val socket: Socket = SocketManager.getSocket() // Reutiliza el socket
    private val tag = "TeacherSocket"

    init {
        socket.on(Events.ON_GET_ALL_TEACHERS_ANSWER.value) { args ->
            val response = args[0] as JSONObject
            val message = response.getString("message")

            val gson = Gson()
            val itemType = object : TypeToken<List<Teacher>>() {}.type
            val teachers = gson.fromJson<List<Meeting>>(message, itemType)


            Log.d(tag, "Received teachers: $teachers")
        }
    }

    fun domyStuff() {
        socket.emit(Events.ON_GET_ALL_TEACHERS.value)
        Log.d(tag, "Requesting all meetings...")
    }
}
