package com.example.myapplication.states

import com.example.myapplication.models.Player

sealed class LobbyState {
    data class GameCreationSuccess(val player: Player, val roomId: String) : LobbyState()
    object GameCreationError : LobbyState()
    object GameCreationLoading : LobbyState()

    data class GameJoinedSuccess(val player: Player, val roomId: String): LobbyState()
    object GameJoinedError : LobbyState()
    object GameJoinedLoading : LobbyState()

    object Idle : LobbyState()
}