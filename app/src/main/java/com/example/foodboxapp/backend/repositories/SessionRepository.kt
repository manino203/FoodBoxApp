package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_sources.SessionDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SessionRepository{
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String, onError: (Throwable) -> Unit)
    suspend fun resumeSession()
    suspend fun logout()

    val state: StateFlow<SessionState>

}

class SessionRepositoryImpl(
    private val dataSource: SessionDataSource,
    private val accountRepository: AccountRepository
): SessionRepository {

    private val _state = MutableStateFlow(SessionState.NOT_LOADED)
    override val state: StateFlow<SessionState> get() = _state.asStateFlow()

    override suspend fun login(email: String, password: String) {
        dataSource.login(email, password).onSuccess {
            accountRepository.update(
                it
            )
            _state.update {
                SessionState.LOGGED_IN
            }
        }.onFailure {
            _state.update {
                SessionState.SESSION_EXPIRED
            }
        }
    }

    override suspend fun register(email: String, password: String, onError: (Throwable) -> Unit) {
        dataSource.register(email, password).onSuccess {
            accountRepository.update(
                it
            )
            _state.update {
                SessionState.LOGGED_IN
            }
        }.onFailure {
            onError(it)
        }
    }

    override suspend fun resumeSession() {
        dataSource.resumeSession().onSuccess {
            accountRepository.update(
                it
            )
            _state.update {
                SessionState.LOGGED_IN
            }
        }.onFailure {
            _state.update {
                SessionState.SESSION_EXPIRED
            }
        }
    }

    override suspend fun logout() {
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