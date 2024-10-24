package com.example.myapplication

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
        private const val GET_ROOM_DATA = "getRoomData" // Add a separate event for this.

        // Socket Event Names - Responses
        private const val GET_ROOM_STATE_SUCCESS = "getRoomStateSuccess"
        private const val GET_ROOM_STATE_FAILURE = "getRoomStateFailure"
        private const val JOIN_SUCCESS = "joinRoomSuccess"

        // Socket Argument Names
        private const val ROOM_ID = "roomId"
        private const val USER_NAME = "username"
        private const val COORDINATES = "coordinates"
        private const val IS_FULL = "isFull"
        private const val YOUR_PLAYER = "yourPlayer"
        private const val CURRENT_PLAYER = "currentPlayer"
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