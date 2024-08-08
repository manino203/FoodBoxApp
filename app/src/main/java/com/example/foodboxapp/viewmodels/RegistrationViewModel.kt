package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.form.FilledRegistrationForm
import com.example.foodboxapp.ui.composables.UiStateError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class RegistrationUiState(
    val loading: Boolean = false,
    val error: UiStateError? = null,
    val username: String = "",
    val email: String = "",
    val password: String = "",
)

class RegistrationViewModel(
    private val sessionRepo: AccountRepository
): ViewModel() {

    val uiState = mutableStateOf(RegistrationUiState())

    fun register(form: FilledRegistrationForm){
        viewModelScope.launch(Main) {
            uiState.value = uiState.value.copy(loading = true)
            withContext(IO){
                sessionRepo.register(form.email, form.password).onFailureWithContext {
                    uiState.value = uiState.value.copy(error = UiStateError(it))
                }
            }
            uiState.value = uiState.value.copy(loading = false)
        }
    }
}


