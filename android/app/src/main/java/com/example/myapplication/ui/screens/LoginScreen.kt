package com.example.myapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.LoginViewModel
import com.example.myapplication.WebSocketManager
import com.example.myapplication.states.LoginState

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit, // Callback to indicate successful login
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }

    // Observe the login state from the ViewModel
    val loginState by loginViewModel.loginState.observeAsState(LoginState.Idle)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter your user name")

        Spacer(modifier = Modifier.height(8.dp))

        // Show error message if login failed
        if (loginState is LoginState.Error) {
            Text(text = "Username Taken", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = username,
            onValueChange = {
                if (loginState == LoginState.Error) loginViewModel.resetState()
                username = it
            },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (username.isNotEmpty()) {
                    loginViewModel.login(username) // Call ViewModel's login function
                }
            },
            enabled = loginState !is LoginState.Loading // Disable button when loading
        ) {
            Text(text = if (loginState is LoginState.Loading) "Logging in..." else "Login")
        }
    }

    // Navigate to the next screen if login is successful
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess()
        }
    }
}
