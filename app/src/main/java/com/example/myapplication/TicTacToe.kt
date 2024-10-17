package com.example.myapplication

import android.app.Application
import androidx.compose.runtime.Applier
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TicTacToe @Inject constructor(): Application() {
}