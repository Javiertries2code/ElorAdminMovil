package com.elorrieta.alumnoclient.socketIO

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

object SocketManager {

    private const val PORT = "4011"
    //   private const val IP = "10.0.22.43"

      private const val IP = "10.0.22.43"
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
