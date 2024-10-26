package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.TicTacToeSession
import com.example.myapplication.models.Coordinates
import com.example.myapplication.models.Player
import com.example.myapplication.models.toPlayer
import com.example.myapplication.states.BoardState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val socket: WebSocketManager,
    private val session: TicTacToeSession
): ViewModel() {

    companion object {
        // Socket Event Names - Requests
        private const val GET_ROOM_DATA = "getRoomData"
        private const val NEW_MOVE = "newMove"

        // Socket Event Names - Responses
        private const val GET_ROOM_STATE_SUCCESS = "getRoomStateSuccess"
        private const val GET_ROOM_STATE_FAILURE = "getRoomStateFailure"
        private const val JOIN_SUCCESS = "joinRoomSuccess"
        private const val NEW_MOVE_FAILURE = "newMoveFailure"
        private const val NEW_MOVE_SUCCESS = "newMoveSucces"

        // Socket Argument Names
        private const val ROOM_ID = "roomId"
        private const val USER_NAME = "username"
        private const val COORDINATES = "coordinates"
        private const val IS_FULL = "isFull"
        private const val YOUR_PLAYER = "yourPlayer"
        private const val CURRENT_PLAYER = "currentPlayer"
        private const val PLAYER = "player"
        private const val X_COORDINATE = "x"
        private const val Y_COORDINATE = "y"
        private const val MESSAGE = "message"
    }

    init {
        setListeners()
    }

    private var currentRoomId: String? = null

    private val _boardState = MutableLiveData<BoardState>(BoardState.Idle)
    val boardState: LiveData<BoardState> = _boardState

    private val _coordinates = MutableLiveData<Coordinates?>(null)
    val coordinates: LiveData<Coordinates?> = _coordinates

    fun enterRoom(roomId: String) {
        this.currentRoomId = roomId
        getRoomData()
    }

    fun newMove(xCoordinate: Int, yCoordinate: Int) {
        if (_boardState.value is BoardState.Playing) {
            val json = JSONObject()
            json.put(ROOM_ID, currentRoomId)
            json.put(PLAYER, (_boardState.value as BoardState.Playing).currentPlayer.name)
            json.put(X_COORDINATE, xCoordinate)
            json.put(Y_COORDINATE, yCoordinate)
            socket.sendMessage(NEW_MOVE, json)
        }
    }

    /**
     *     fun setNewMoveListener(listener: (MoveData) -> Unit) {
     *         socket?.on(SUCCESSFULLY_ADDED) { args ->
     *             if (args.isNotEmpty()) {
     *                 val message = args[0] as JSONObject
     *                 val player = message.getString(PLAYER_KEY).toPlayer()
     *                 if (player != null) {
     *                     val moveData = MoveData(
     *                         player = player,
     *                         xCoordinate = message.getInt(X_KEY),
     *                         yCoordinate = message.getInt(Y_KEY)
     *                     )
     *
     *                     listener(moveData)
     *                 }
     *             }
     *         }
     *     }
     */

    private fun setListeners() {
        socket.setListener(GET_ROOM_STATE_SUCCESS) {
            it.validSocketArguments()?.let { json ->
                val coordinatesJson = json.getJSONArray(COORDINATES)
                val coordinates = parseCoordinates(coordinatesJson)
                // Check if coordinates are different before updating
                _coordinates.postValue(coordinates)

                val canPlay = json.getBoolean(IS_FULL)
                if (!canPlay) {
                    _boardState.postValue(BoardState.WaitingForOtherPlayer)
                } else {
                    val yourPlayer = json.getString(YOUR_PLAYER).toPlayer()
                    val currentPlayer = json.getString(CURRENT_PLAYER).toPlayer()

                    if (yourPlayer == null || currentPlayer == null) {
                        // Handle error
                    } else {
                        _boardState.postValue(BoardState.Playing(yourPlayer, currentPlayer))
                    }
                }

            }
        }

        socket.setListener(JOIN_SUCCESS) {
            it.validSocketArguments()?.let { json ->
                val roomId = json.getString(ROOM_ID)
                if (roomId == currentRoomId) {
                    getRoomData()
                }
            }
        }

        socket.setListener(NEW_MOVE_SUCCESS) {
            it.validSocketArguments()?.let { json ->
                val roomId = json.getString(ROOM_ID)
                if (roomId != this.currentRoomId) {
                    return@let
                }

                val coordinatesJson = json.getJSONArray(COORDINATES)
                val coordinates = parseCoordinates(coordinatesJson)
                // Check if coordinates are different before updating
                _coordinates.postValue(coordinates)

                val currentPlayer = json.getString(CURRENT_PLAYER).toPlayer()

                if (currentPlayer == null) {
                    // Handle error
                } else {
                    // Improve this. Can be one line somehow
                    if (_boardState.value is BoardState.Playing) {
                        val copy = (_boardState.value as BoardState.Playing)
                            .copy(currentPlayer = currentPlayer)
                        _boardState.postValue(copy)
                    } else {
                        // handle error
                    }
                }
            }
        }

        socket.setListener(NEW_MOVE_FAILURE) {
            it.validSocketArguments()?.let { json ->
                val message = json.getString(MESSAGE)
                Log.d("BoardViewModel", message)
            }
        }
    }

    private fun getRoomData() {
        val json = JSONObject()
        json.put(USER_NAME, session.userName)
        json.put(ROOM_ID, currentRoomId)
        socket.sendMessage(GET_ROOM_DATA, json)
    }

    private fun parseCoordinates(coordinatesJson: JSONArray): Coordinates {
        val coordinates = Coordinates()
        for (i in 0 until coordinatesJson.length()) {
            val row = coordinatesJson.getJSONArray(i)
            for (j in 0 until row.length()) {
                val player = row.optString(j, null)?.toPlayer()
                coordinates.coordinates[i][j] = player
            }
        }
        return coordinates
    }
}