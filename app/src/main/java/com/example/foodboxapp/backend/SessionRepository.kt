package com.example.foodboxapp.backend

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SessionRepository{
    fun login(username: String, password: String)
    suspend fun resumeSession()
    fun logout()

    val state: StateFlow<SessionState>

}

class SessionRepositoryImpl(
    private val dataSource: SessionDataSource
): SessionRepository{
    private val _state = MutableStateFlow(SessionState.NOT_LOADED)
    override val state: StateFlow<SessionState> get() = _state.asStateFlow()
    override fun login(username: String, password: String) {
        dataSource.login(username, password).onSuccess {
            _state.update {
                SessionState.LOGGED_IN
            }
        }
    }

    override suspend fun resumeSession() {
        delay(500)
        _state.update {
            SessionState.LOGGED_IN
        }
    }

    override fun logout() {
        dataSource.logout()
        _state.update {
            SessionState.LOGGED_OUT
        }
    }

}

enum class SessionState{
    NOT_LOADED,
    SESSION_AVAILABLE,
    NOT_LOGGED_IN,
    LOGGED_IN,
    LOGGED_OUT,
    SESSION_EXPIRED
}