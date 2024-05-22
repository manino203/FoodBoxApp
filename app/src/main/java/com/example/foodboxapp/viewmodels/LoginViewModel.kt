package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodboxapp.backend.SessionRepository
import com.example.foodboxapp.backend.SessionRepositoryImpl
import com.example.foodboxapp.ui.composables.UiStateError
import java.io.Closeable
import java.nio.channels.UnresolvedAddressException

data class LoginUiState(
    val isLoggingIn: Boolean = false,
    val error: UiStateError? = null
)

class LoginViewModel(
    private val sessionRepository: SessionRepositoryImpl
): ViewModel() {
    val uiState = mutableStateOf(LoginUiState())

    fun login(username: String, password: String){
        sessionRepository.login(username, password)
    }

    fun forgottenPassword(){

    }

}