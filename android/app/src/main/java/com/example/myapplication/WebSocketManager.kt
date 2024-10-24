package com.example.myapplication

import com.example.myapplication.models.MoveData
import com.example.myapplication.models.Player
import com.example.myapplication.models.toPlayer
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject


class WebSocketManager(private val socket: Socket?  = null) {

    companion object {
        private const val MESSAGE_FIELD = "message"
        private const val ID_FIELD = "id"

        // Socket Events
        private const val START_GAME = "startGame"
        private const val INITIAL_STATE = "initialState"
        private const val NEW_MOVE = "newMove"
        private const val SUCCESSFULLY_ADDED = "successfullyAdded"

        // Socket data
        private const val PLAYER_KEY = "player"
        private const val X_KEY = "x"
        private const val Y_KEY = "y"
    }

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

    fun sendMoveData(moveData: MoveData) {
        val json = JSONObject()
        json.put(PLAYER_KEY, moveData.player)
        json.put(X_KEY, moveData.xCoordinate)
        json.put(Y_KEY, moveData.yCoordinate)
        socket?.emit(NEW_MOVE, json)
    }

    fun setNewMoveListener(listener: (MoveData) -> Unit) {
        socket?.on(SUCCESSFULLY_ADDED) { args ->
            if (args.isNotEmpty()) {
                val message = args[0] as JSONObject
                val player = message.getString(PLAYER_KEY).toPlayer()
                if (player != null) {
                    val moveData = MoveData(
                        player = player,
                        xCoordinate = message.getInt(X_KEY),
                        yCoordinate = message.getInt(Y_KEY)
                    )

                    listener(moveData)
                }
            }
        }
    }

    fun setStartGameListener(listener: (Player) -> Unit) {
        socket?.on(START_GAME) { args ->
            if (args.isNotEmpty()) {
                val message = args[0] as JSONObject

                message.getString(PLAYER_KEY).toPlayer()?.let { listener(it) }
            }
        }
    }

}
