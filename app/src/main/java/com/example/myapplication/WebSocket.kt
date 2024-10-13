package com.example.myapplication

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


class WebSocket {
    private var socket: Socket?  = null

    companion object {
        private const val MESSAGE_FIELD = "message"
        private const val ID_FIELD = "id"
        private const val NEW_MESSAGE_EVENT = "newMessage"
    }

    init {
        try {
            socket = IO.socket("https://chatserver-b2ardddcb6dsevbt.westus-01.azurewebsites.net/")
        } catch (e: Exception) {
            print(e.message)
        }
    }

    fun connect() {
        socket?.connect()
    }

    fun sendMessage(message: String) {
        val json = JSONObject()
        json.put(MESSAGE_FIELD, message)
        json.put(ID_FIELD, socket?.id())
        socket?.emit("newMessage", json)
    }

    fun setNewMessageListener(listener: (MessageData) -> Unit) {
        socket?.on(NEW_MESSAGE_EVENT, { args ->
            if (args.isNotEmpty()) {
                val message = args[0] as JSONObject

                val messageType = if (message.getString(ID_FIELD) == socket?.id()) {
                    MessageType.SENT
                } else {
                    MessageType.RECEIVED
                }
                listener(MessageData(message.getString(MESSAGE_FIELD), messageType))
            }
        })
    }

}

enum class MessageType {
    RECEIVED,
    SENT
}

data class MessageData(val message: String, val type: MessageType) {
}
