package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.TicTacToeSession
import com.example.myapplication.states.LobbyState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val socket: WebSocketManager,
    private val session: TicTacToeSession
): ViewModel() {

    companion object {
        // Socket Event Names - Requests
        private const val CREATE_ROOM = "createNewGameRoom"

        // Socket Event Names - Responses
        private const val CREATION_SUCCESS = "gameRoomCreationSuccess"
        private const val CREATION_FAILED = "gameRoomCreationFailure"

        // Socket Argument Names
        private const val USER_NAME = "username"
        private const val ROOM_ID = "roomId"
        private const val MESSAGE = "message"
    }

    init {
        Log.d("LobbyViewModel", "Initializing LobbyViewModel")
        setListeners()
    }

    private val _lobbyState = MutableLiveData<LobbyState>(LobbyState.Idle)
    val lobbyState: LiveData<LobbyState> = _lobbyState

    fun getUserName(): String = session.userName

    fun createNewGame() {
        val json = JSONObject()
        json.put(USER_NAME, getUserName())
        socket.sendMessage(CREATE_ROOM, json)
        _lobbyState.postValue(LobbyState.GameCreationLoading)
    }

    fun joinGame(roomNumber: String) { }

    private fun setListeners() {
        socket.setListener(CREATION_SUCCESS) {
            val json = it.validSocketArguments()
            if (json != null) {
                val roomId = json.getString(ROOM_ID)
                Log.d("LobbyViewModel", "Game creation success - $roomId")
                _lobbyState.postValue(LobbyState.GameCreationSuccess)
            } else {
                Log.d("LobbyViewModel", "Game creation failure - null args")
                _lobbyState.postValue(LobbyState.GameCreationError)
            }
        }
        socket.setListener(CREATION_FAILED) {
            var errorMessage = ""
            val json = it.validSocketArguments()
            if (json != null) {
                errorMessage = json.getString(MESSAGE)
            }
            Log.d("LobbyViewModel", "Game creation failure - $errorMessage")
            _lobbyState.postValue(LobbyState.GameCreationError)
        }
    }
}