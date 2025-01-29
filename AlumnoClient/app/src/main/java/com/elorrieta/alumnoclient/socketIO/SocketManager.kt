package com.elorrieta.alumnoclient.socketIO

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

object SocketManager {

    private const val PORT = "4005"
    private const val IP = "192.168.1.40"
    private const val SERVER_URL = "http://$IP:$PORT"

    private var socket: Socket? = null

    fun getSocket(): Socket {
        if (socket == null) {
            socket = IO.socket(SERVER_URL)
            Log.d("SocketManager", "Socket initialized")
        }
        return socket!!
    }
}
