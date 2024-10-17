package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.myapplication.TicTacToeBoard
import com.example.myapplication.WebSocketManager
import com.example.myapplication.models.Coordinates
import com.example.myapplication.models.MoveData
import com.example.myapplication.models.Player
import com.example.myapplication.models.toOpponent

@Composable
fun BoardScreen(modifier: Modifier,  player: Player, roomId: String) {
    val currentPlayer = remember { mutableStateOf<Player?>(null) }
    val coordinates = remember { mutableStateOf<Coordinates>(Coordinates()) }

//    socket.setNewMoveListener { moveData ->
//        coordinates.value.updateCoordinates(moveData)
//        currentPlayer.value = moveData.player.toOpponent()
//    }
//    socket.setStartGameListener {
//        currentPlayer.value = it
//    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "Room: $roomId")
        if (currentPlayer.value != null) {
            Text(text = "You Are: $player")
            Text(text = "Current Player: ${currentPlayer.value}")
            TicTacToeBoard(
                coordinates = coordinates.value,
                onCoordinateTake = { x, y ->
                    if (currentPlayer.value != null) {
//                        socket.sendMoveData(
//                            MoveData(
//                                player = currentPlayer.value!!,
//                                xCoordinate = x,
//                                yCoordinate = y
//                            )
//                        )
                    }
                },
                canPlay = player == currentPlayer.value && currentPlayer.value != null
            )
        } else {
            Text(text = "Waiting for other players")
        }
    }
}