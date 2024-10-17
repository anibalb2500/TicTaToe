package com.example.myapplication.states

sealed class LobbyState {
    object GameCreationSuccess : LobbyState()
    object GameCreationError : LobbyState()
    object GameCreationLoading : LobbyState()

    object GameJoinedSuccess: LobbyState()
    object GameJoinedError : LobbyState()
    object GameJoinedLoading : LobbyState()

    object Idle : LobbyState()
}