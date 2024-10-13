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

@Composable
fun TicTacToeBoard() {
    var board by remember { mutableStateOf(List(3) { row -> List(3) { col -> Cell(row, col) } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        board.forEach { row ->
            Row {
                row.forEach { cell ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White)
                            .border(2.dp, Color.Black)
                            .background(Color.LightGray)
                            .clickable(enabled = cell.value.isEmpty() && winner == null) {
//                                cell.value = currentPlayer
//                                if (checkWin(board, currentPlayer)) {
//                                    winner = currentPlayer
//                                } else if (board.flatten().all { it.value.isNotEmpty() }) {
//                                    winner = "Draw"
//                                } else {
//                                    currentPlayer = if (currentPlayer == "X") "O" else "X"
//                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
//                        Text(text = cell.value, fontSize = 24.sp)
                    }
                }
            }
        }
        winner?.let {
            Text(text = "Winner: $it", fontSize = 24.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

fun checkWin(board: List<List<Cell>>, player: String): Boolean {
    // Check rows, columns, and diagonals
    return (board.any { row -> row.all { it.value == player } } ||
            (0..2).any { col -> board.all { row -> row[col].value == player } } ||
            (0..2).all { i -> board[i][i].value == player } ||
            (0..2).all { i -> board[i][2 - i].value == player })
}