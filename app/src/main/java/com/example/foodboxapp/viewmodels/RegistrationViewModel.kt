package com.example.foodboxapp.viewmodels

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.form.FilledRegistrationForm
import com.example.foodboxapp.ui.composables.UiStateError
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class RegistrationUiState(
    val loading: Boolean = false,
    val error: UiStateError? = null,
    val username: String = "",
    val email: String = "",
    val password: String = "",
)

class RegistrationViewModel: ViewModel() {

    val uiState = mutableStateOf(RegistrationUiState())

    fun register(form: FilledRegistrationForm){
        viewModelScope.launch {
            uiState.value = uiState.value.copy(loading = true)
            delay(3000)
            uiState.value = uiState.value.copy(loading = false)
            uiState.value = uiState.value.copy(error = UiStateError(Exception("not implemented yet")))
        }

    }

}


