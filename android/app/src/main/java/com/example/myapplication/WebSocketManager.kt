package com.example.myapplication

import com.example.myapplication.models.MoveData
import com.example.myapplication.models.Player
import com.example.myapplication.models.toPlayer
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject


class WebSocketManager(private val socket: Socket?  = null) {

    companion object {}

    init {
        socket?.connect()
    }

    fun sendMessage(eventName: String, data: JSONObject?) {
        if (data != null) {
            socket?.emit(eventName, data)
        } else {
            socket?.emit(eventName)
        }
    }

    fun setListener(event: String, callback: (data: Any) -> Unit) {
        socket?.on(event) { args -> callback(args) }
    }
}
