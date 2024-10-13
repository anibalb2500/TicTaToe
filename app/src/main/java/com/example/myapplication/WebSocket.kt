package com.example.myapplication

import com.example.myapplication.models.MoveData
import com.example.myapplication.models.Player
import com.example.myapplication.models.toPlayer
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject


class WebSocket {
    private var socket: Socket?  = null

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

        // Misc
        private const val LOCAL_HOST = "http://10.0.2.2:3000"
        private const val LIVE = "https://chatserver-b2ardddcb6dsevbt.westus-01.azurewebsites.net/"
    }

    init {
        try {
            socket = IO.socket(LOCAL_HOST)
        } catch (e: Exception) {
            print(e.message)
        }
    }

    fun connect() {
        socket?.connect()
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
//        socket?.on(NEW_MESSAGE_EVENT) { args ->
//            if (args.isNotEmpty()) {
//                val message = args[0] as JSONObject
//
//                val x = 0//message.getString(MESSAGE_FIELD), messageType
//                val y = 0
//                val player = Player.X // Somehting
//                listener(MoveData(player, x, y))
//            }
//        }
    }

    fun setInitialStateListener(listener: (Player) -> Unit) {
        socket?.on(INITIAL_STATE) { args ->
            if (args.isNotEmpty()) {
                val message = args[0] as JSONObject

                message.getString(PLAYER_KEY).toPlayer()?.let {
                    listener(it)
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
