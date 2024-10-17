package com.example.myapplication.states

sealed class LoginState {
    object Success : LoginState()
    object Error : LoginState()
    object Loading : LoginState()
    object Idle : LoginState()
}