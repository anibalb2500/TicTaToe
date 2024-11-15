package com.example.myapplication.states

import com.example.myapplication.models.Player

sealed class  BoardState {
    object Idle : BoardState()
    object WaitingForOtherPlayer : BoardState()
    object OpponentLeft : BoardState()
    data class Playing(
        val yourPlayer: Player,
        val currentPlayer: Player
    ) : BoardState()

}