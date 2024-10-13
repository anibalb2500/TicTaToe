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
    val messages = remember { mutableStateListOf<MessageData>() }
    socket.connect()
    socket.setNewMessageListener { message ->
        messages.add(message)
    }

    Column(modifier = modifier.fillMaxSize()) {
        MessageList(messages = messages, modifier = Modifier.weight(1f))
        SendMessageField { socket.sendMessage(it) }
    }
}

@Composable
fun SendMessageField(onSendMessage: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        // TextField for user input
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter text") },
            modifier = Modifier.weight(1f)
        )

        // Button placed next to the TextField
        Button(
            onClick = { onSendMessage(text) },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text("Submit")
        }
    }
}

// MessageList composable
@Composable
fun MessageList(messages: List<MessageData>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(messages) { message ->
            Text(text = message.message, modifier = Modifier.padding(8.dp))
        }
    }
}
