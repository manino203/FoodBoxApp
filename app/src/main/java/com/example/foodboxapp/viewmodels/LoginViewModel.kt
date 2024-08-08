package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.ui.composables.UiStateError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class LoginUiState(
    val isLoggingIn: Boolean = false,
    val error: UiStateError? = null
)

class LoginViewModel(
    private val accountRepository: AccountRepository
): ViewModel() {
    val uiState = mutableStateOf(LoginUiState())

    fun login(username: String, password: String){
        viewModelScope.launch(Dispatchers.Main){
            uiState.value = uiState.value.copy(isLoggingIn = true)
            withContext(IO){
                accountRepository.login(username, password).onFailureWithContext {
                    uiState.value = uiState.value.copy(error = UiStateError(it))
                }
            }
            uiState.value = uiState.value.copy(isLoggingIn = false)
        }
    }
}