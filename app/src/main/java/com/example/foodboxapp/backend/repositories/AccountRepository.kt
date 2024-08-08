package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_sources.AccountDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface AccountRepository{
    suspend fun login(email: String, password: String): Result<Account>
    suspend fun register(email: String, password: String): Result<Account>
    suspend fun update(account: Account): Result<Account>
    suspend fun resumeSession(): Result<Account>
    suspend fun logout(): Result<Unit>

    val state: StateFlow<SessionState>
    val account: StateFlow<Account?>
}

class AccountRepositoryImpl(
    private val dataSource: AccountDataSource,
): AccountRepository {

    private val _state = MutableStateFlow(SessionState.NOT_LOADED)
    override val state: StateFlow<SessionState> get() = _state.asStateFlow()

    private val _account = MutableStateFlow<Account?>(null)
    override val account: StateFlow<Account?> get() = _account.asStateFlow()

    override suspend fun login(email: String, password: String): Result<Account> {
        return dataSource.login(email, password).onSuccess { acc ->
            _account.update{
                acc
            }
            _state.update {
                SessionState.LOGGED_IN
            }
        }
    }

    override suspend fun register(email: String, password: String): Result<Account> {
        return dataSource.register(email, password).onSuccess { acc ->
            _account.update { acc }
            _state.update {
                SessionState.LOGGED_IN
            }
        }
    }

    override suspend fun update(account: Account): Result<Account> {
        return dataSource.update(account).onSuccess { acc ->
            _account.update { acc }
        }
    }

    override suspend fun resumeSession(): Result<Account> {
        return dataSource.resumeSession().onSuccess { acc ->
            _account.update { acc }
            _state.update {
                SessionState.LOGGED_IN
            }
        }.onFailure{
            _account.update { null }
            _state.update {
                SessionState.LOGGED_OUT
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        return dataSource.logout().onSuccess {
            _account.update {
                null
            }
            _state.update {
                SessionState.LOGGED_OUT
            }
        }
    }
}

enum class SessionState{
    LOGGED_IN,
    LOGGED_OUT,
    NOT_LOADED
}