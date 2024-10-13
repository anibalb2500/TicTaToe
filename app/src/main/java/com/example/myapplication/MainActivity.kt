package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.models.MoveData
import com.example.myapplication.models.Player
import com.example.myapplication.models.toOpponent
import com.example.myapplication.ui.theme.MyApplicationTheme
import io.socket.client.IO
import java.net.Socket

class MainActivity : ComponentActivity() {

    private val webSocket = WebSocket()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Screen(
                        modifier = Modifier.padding(innerPadding),
                        socket =  webSocket
                    )
                }
            }
        }
    }
}

@Composable
fun Screen(modifier: Modifier, socket: WebSocket) {
    val currentPlayer = remember { mutableStateOf<Player?>(null) }
    val coordinates = remember { mutableStateOf<Coordinates>(Coordinates()) }
    var yourPlayer = remember { mutableStateOf<Player?>(null) }

    socket.connect()
    socket.setInitialStateListener {
        yourPlayer.value = it
    }
    socket.setNewMoveListener { moveData ->
        coordinates.value.updateCoordinates(moveData)
        currentPlayer.value = moveData.player.toOpponent()
    }
    socket.setStartGameListener {
        currentPlayer.value = it
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (currentPlayer.value != null) {
            Text(text = "You Are: ${yourPlayer.value}")
            Text(text = "Current Player: ${currentPlayer.value}")
            TicTacToeBoard(
                coordinates = coordinates.value,
                onCoordinateTake = { x, y ->
                    if (currentPlayer.value != null) {
                        socket.sendMoveData(MoveData(
                            player = currentPlayer.value!!,
                            xCoordinate = x,
                            yCoordinate = y
                        ))
                    }
                },
                canPlay = yourPlayer.value == currentPlayer.value && currentPlayer.value != null
            )
        } else {
            Text(text = "Esperando otros jugadores")
        }
    }
}


data class Cell(val row: Int, val col: Int, var value: String = "")



class Coordinates {

    val coordinates: Array<Array<Player?>> = arrayOf(
        arrayOf(null, null, null),
        arrayOf(null, null, null),
        arrayOf(null, null, null)
    )

    fun updateCoordinates(moveData: MoveData) {
        coordinates[moveData.xCoordinate][moveData.yCoordinate] = moveData.player

    }
}