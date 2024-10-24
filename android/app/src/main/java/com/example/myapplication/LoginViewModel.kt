package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.TicTacToeSession
import com.example.myapplication.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val socket: WebSocketManager,
    private val session: TicTacToeSession
): ViewModel() {

    companion object {
        // Socket Event Names - Requests
        private const val LOGIN = "login"

        // Socket Event Names - Responses
        private const val LOGIN_SUCCESS = "loginSuccess"
        private const val LOGIN_FAILED = "loginFailed"

        // Socket Argument Names
        private const val USER_NAME = "username"
    }

    init {
        setListeners()
    }

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(userName: String) {
        val json = JSONObject()
        json.put(USER_NAME, userName)
        socket.sendMessage(LOGIN, json)
        _loginState.postValue(LoginState.Loading)
    }

    fun resetState() {
        _loginState.postValue(LoginState.Idle)
    }

    private fun setListeners() {
        socket.setListener(LOGIN_SUCCESS) { it ->
            val json = it.validSocketArguments()
            if (json != null) {
                session.userName = json.getString(USER_NAME)
            }

            _loginState.postValue(LoginState.Success)
        }
        socket.setListener(LOGIN_FAILED) { _loginState.postValue(LoginState.Error) }
    }
}