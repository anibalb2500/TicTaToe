package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.LobbyViewModel
import com.example.myapplication.models.Player
import com.example.myapplication.states.LobbyState

@Composable
fun LobbyScreen(
    modifier: Modifier = Modifier,
    onGameCreationSuccess: (String, String) -> Unit,
    lobbyViewModel: LobbyViewModel = hiltViewModel(), // Assuming a ViewModel for Lobby exists
) {
    val username = lobbyViewModel.getUserName()
    var roomNumber by remember { mutableStateOf("") }
    val lobbyState by lobbyViewModel.lobbyState.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to the Lobby ${username}", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Create New Game Button
        Button(
            onClick = {
                if (username.isNotEmpty()) {
                    lobbyViewModel.createNewGame()
//                    navController.navigate("currentGame")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = username.isNotEmpty() // Button is enabled only if username is not empty
        ) {
            Text(text = "Create New Game")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Join Existing Game Section
        Text(text = "Join an Existing Game", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = roomNumber,
            onValueChange = { roomNumber = it },
            label = { Text("Enter Room Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (username.isNotEmpty() && roomNumber.isNotEmpty()) {
                    lobbyViewModel.joinGame(roomNumber) // Join game with username and room number
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = username.isNotEmpty() && roomNumber.isNotEmpty() // Button is enabled only if both username and room number are filled
        ) {
            Text(text = "Join Game")
        }
    }

    // Navigate to the next screen if login is successful
    LaunchedEffect(lobbyState) {
        if (lobbyState is LobbyState.GameCreationSuccess) {
            val state = lobbyState as LobbyState.GameCreationSuccess
            onGameCreationSuccess(state.player.name, state.roomId)
        }

        if (lobbyState is LobbyState.GameJoinedSuccess) {
            val state = lobbyState as LobbyState.GameJoinedSuccess
            onGameCreationSuccess(state.player.name, state.roomId)
        }
    }
}
