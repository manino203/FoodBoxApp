package com.example.foodboxapp.backend.repositories

import android.util.Log
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_sources.AccountDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface AccountRepository{
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun update(account: Account)
    suspend fun resumeSession()
    suspend fun logout()

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

    override suspend fun login(email: String, password: String) {
        _account.update{
            dataSource.login(email, password)
        }
        _state.update {
            SessionState.LOGGED_IN
        }
    }

    override suspend fun register(email: String, password: String) {
        _account.update{ dataSource.register(email, password) }
        _state.update {
            SessionState.LOGGED_IN
        }
    }

    override suspend fun update(account: Account) {
        _account.update { dataSource.update(account) }
    }

    override suspend fun resumeSession() {
        try{
            _account.update { dataSource.resumeSession() }
            _state.update {
                SessionState.LOGGED_IN
            }
        }catch (e: Exception){
            Log.d("resumeSession", "${e.message}")
            _state.update {
                SessionState.SESSION_EXPIRED
            }
        }
    }

    override suspend fun logout() {
        dataSource.logout()
        _account.update {
            null
        }
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