package com.elorrieta.alumnoclient.socketIO


import com.elorrieta.alumnoclient.socketIO.SocketManager


import android.app.Activity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.elorrieta.alumnoclient.R
import com.elorrieta.alumnoclient.entity.Meeting
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



class MeetingSocket(private val activity: Activity) {

    private val socket: Socket = SocketManager.getSocket() // Reutiliza el socket
    private val tag = "MeetingSocket"

    init {
        socket.on(Events.ON_GET_ALL_MEETINGS_ANSWER.value) { args ->
            val response = args[0] as JSONObject
            val message = response.getString("message")

            val gson = Gson()
            val itemType = object : TypeToken<List<Meeting>>() {}.type
            val meetings = gson.fromJson<List<Meeting>>(message, itemType)

            activity.runOnUiThread {
                activity.findViewById<TextView>(R.id.textView).append("\nStudents: $meetings")
            }
            Log.d(tag, "Received students: $meetings")
        }
    }

    fun domyStuff() {
        socket.emit(Events.ON_GET_ALL_MEETINGS.value)
        Log.d(tag, "Requesting all meetings...")
    }
}
