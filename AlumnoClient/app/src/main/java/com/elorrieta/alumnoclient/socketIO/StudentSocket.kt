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



class StudentSocket(private val activity: Activity) {

    private val socket: Socket = SocketManager.getSocket() // Reutiliza el socket
    private val tag = "StudentSocket"

    init {
        socket.on(Events.ON_GET_ALL_STUDENTS_ANSWER.value) { args ->
            val response = args[0] as JSONObject
            val message = response.getString("message")

            val gson = Gson()
            val itemType = object : TypeToken<List<Student>>() {}.type
            val students = gson.fromJson<List<Student>>(message, itemType)

            activity.runOnUiThread {
                activity.findViewById<TextView>(R.id.textView).append("\nStudents: $students")
            }
            Log.d(tag, "Received students: $students")
        }
    }

    fun doGetAll() {
        socket.emit(Events.ON_GET_ALL_STUDENTS.value)
        Log.d(tag, "Requesting all students...")
    }
}
