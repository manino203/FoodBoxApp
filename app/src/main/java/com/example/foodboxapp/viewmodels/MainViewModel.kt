package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.backend.repositories.SessionState
import com.example.foodboxapp.ui.composables.UiStateError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


data class MainUiState(
    val loading: Boolean = false,
    val sessionState: SessionState = SessionState.NOT_LOADED,
    val account: Account? = null,
    val error: UiStateError? = null
)

class MainViewModel(
    private val sessionRepo: AccountRepository,
    private val accountRepo: AccountRepository
): ViewModel() {
    val uiState = mutableStateOf(MainUiState())

    fun logout(){
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepo.logout().onFailure { uiState.value = uiState.value.copy(error = UiStateError(it)) }
        }
    }

    fun resumeSession(){
        viewModelScope.launch(Dispatchers.IO){
            sessionRepo.resumeSession().onFailure { uiState.value = uiState.value.copy(error = UiStateError(it)) }
        }
    }

    fun collectChanges(){
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.account.collect{
                uiState.value = uiState.value.copy(account = it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepo.state.collectLatest {
                uiState.value = uiState.value.copy(sessionState = it)
            }
        }
    }
}