package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.models.Coordinates
import com.example.myapplication.models.MoveData
import com.example.myapplication.models.Player
import com.example.myapplication.models.toOpponent
import com.example.myapplication.ui.screens.BoardScreen
import com.example.myapplication.ui.screens.LobbyScreen
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN,
        modifier = modifier
    ) {
        composable(Destinations.LOGIN) {
            LoginScreen(
                modifier = Modifier.fillMaxSize(),
                onLoginSuccess = { navController.navigate(Destinations.LOBBY) }
            )
        }
        composable(Destinations.LOBBY) {
            LobbyScreen(
                onGameCreationSuccess = { player, roomId ->
                    navController.navigate(Destinations.boardRoute(player, roomId))
                },
            )
        }
        composable(
            route = Destinations.BOARD,
            arguments = listOf(
                navArgument("player") { type = NavType.StringType },
                navArgument("roomId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val player = Player.valueOf(backStackEntry.arguments?.getString("player") ?: "")
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            BoardScreen(modifier = Modifier.fillMaxSize(), player = player, roomId = roomId)
        }

    }
}

object Destinations {
    const val LOGIN = "login"
    const val LOBBY = "lobby"
    const val BOARD = "board/{player}/{roomId}"

    fun boardRoute(player: String, roomId: String): String {
        return "board/${player}/${roomId}"
    }
}