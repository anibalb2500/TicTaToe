package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.models.Coordinates
import com.example.myapplication.models.Player

@Composable
fun TicTacToeBoard(
    coordinates: Coordinates,
    onCoordinateTake: (Int, Int) -> Unit,
    canPlay: Boolean
) {
    var winner by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        coordinates.coordinates.forEachIndexed { xCoordinate, row ->
            Row {
                row.forEachIndexed { yCoordinate, tileOwner ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White)
                            .border(2.dp, Color.Black)
                            .background(Color.LightGray)
                            .clickable(enabled = tileOwner == null && canPlay) {
                                onCoordinateTake(xCoordinate, yCoordinate)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (tileOwner != null) {
                            val text = if (tileOwner == Player.X) "X" else "O"
                            Text(text = text, fontSize = 24.sp)
                        }
                    }
                }
            }
        }
        winner?.let {
            Text(text = "Winner: $it", fontSize = 24.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}