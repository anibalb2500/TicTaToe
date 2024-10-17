package com.example.myapplication.di

import android.util.Log
import com.example.myapplication.WebSocketManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    private const val TAG = "WebSocketModule"
    private const val LOCAL_HOST = "http://10.0.2.2:3000"
    private const val LIVE = "https://chatserver-b2ardddcb6dsevbt.westus-01.azurewebsites.net/"

    @Provides
    fun provideWebSocket(): Socket? = try {
        Log.d(TAG, "Initializing the websocket")
        IO.socket(LOCAL_HOST)
    } catch (e: Exception) {
        Log.e(TAG, "Failed to initialize the websocket: ${e.message ?: "UNKNOWN ERROR"}")
        null
    }

        @Provides
        fun provideWebSocketManager(socket: Socket?): WebSocketManager = WebSocketManager(socket)
}
