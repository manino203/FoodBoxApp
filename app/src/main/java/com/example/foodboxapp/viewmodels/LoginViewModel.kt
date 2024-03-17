package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodboxapp.ui.composables.UiStateError
import java.nio.channels.UnresolvedAddressException

data class LoginUiState(
    val isLoggingIn: Boolean = false,
    val error: UiStateError? = null
)

class LoginViewModel: ViewModel() {

    val uiState = mutableStateOf(LoginUiState())

    fun login(username: String, password: String){
        uiState.value = uiState.value.copy(error = UiStateError(UnresolvedAddressException()))
    }

    fun forgottenPassword(){

    }

}