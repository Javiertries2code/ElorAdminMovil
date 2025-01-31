package com.elorrieta.alumnoclient.socketIO

import android.content.Context
import android.util.Log
import com.elorrieta.alumnoclient.entity.Meeting
import com.elorrieta.socketsio.sockets.config.Events
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.Socket
import org.json.JSONObject


class RegisterSocket(private val context: Context)
{

    private val socket: Socket = SocketManager.getSocket() // Reutiliza el socket
    private val tag = "MeetingSocket"

    init {


        socket.on(Events.ON_GET_ALL_MEETINGS_ANSWER.value) { args ->
            val response = args[0] as JSONObject
            val message = response.getString("message")

            val gson = Gson()
            val itemType = object : TypeToken<List<Meeting>>() {}.type
            val meetings = gson.fromJson<List<Meeting>>(message, itemType)

            Log.d(tag, "Received students: $meetings")
        }

    }

    fun domyStuff() {
        socket.emit(Events.ON_GET_ALL_MEETINGS.value)
        Log.d(tag, "Requesting all meetings...")
    }

    fun registerUser() {

    }


}