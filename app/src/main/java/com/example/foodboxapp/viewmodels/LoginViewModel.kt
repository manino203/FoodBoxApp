package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.repositories.SessionRepository
import com.example.foodboxapp.ui.composables.UiStateError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoggingIn: Boolean = false,
    val error: UiStateError? = null
)

class LoginViewModel(
    private val sessionRepository: SessionRepository
): ViewModel() {
    val uiState = mutableStateOf(LoginUiState())

    fun login(username: String, password: String){
        viewModelScope.launch(Dispatchers.IO){
            uiState.value = uiState.value.copy(isLoggingIn = true)
            kotlin.runCatching{
                sessionRepository.login(username, password)
            }.onFailure {
                uiState.value = uiState.value.copy(error = UiStateError(it))
            }
        }.invokeOnCompletion {
            uiState.value = uiState.value.copy(isLoggingIn = false)
        }
    }

    fun forgottenPassword(){

    }

}