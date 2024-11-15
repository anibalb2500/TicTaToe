package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.BoardViewModel
import com.example.myapplication.TicTacToeBoard
import com.example.myapplication.models.Coordinates
import com.example.myapplication.models.Player
import com.example.myapplication.states.BoardState

@Composable
fun BoardScreen(
    modifier: Modifier,
    player: Player,
    roomId: String,
    boardViewModel: BoardViewModel = hiltViewModel(),
) {
    val boardState by boardViewModel.boardState.observeAsState()
    val coordinates by boardViewModel.coordinates.observeAsState()

    LaunchedEffect(Unit) {
        boardViewModel.enterRoom(roomId)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "Room: $roomId")

        when (boardState) {
            is BoardState.Idle -> {
                Text(text = "Waiting for other players")
            }
            is BoardState.WaitingForOtherPlayer -> {
                Text(text = "Waiting for other players")
            } else -> {
                if (boardState != null) {
                    Playing(player, boardState!!, coordinates!!) { x, y ->
                        boardViewModel.newMove(x, y)
                    }
                }
            }
        }
    }
}

@Composable
private fun Playing(
    player: Player,
    boardState: BoardState,
    coordinates: Coordinates,
    onNewMove: (Int, Int) -> Unit
) {
    var canPlay = false
    if (boardState is BoardState.Playing) {
        canPlay = player == boardState.currentPlayer
        Text(text = "You Are: $player")
        Text(text = "Current Player: ${boardState.currentPlayer}")
    } else if (boardState is BoardState.OpponentLeft) {
        Text(text = "Your opponent left. Sorry, bud.")
    }
    TicTacToeBoard(
        coordinates = coordinates,
        onCoordinateTake = { x, y -> onNewMove(x, y)},
        canPlay = canPlay
    )
}