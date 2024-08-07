package com.example.foodboxapp.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.Address
import com.example.foodboxapp.backend.data_holders.PaymentMethod
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.ui.composables.UiStateError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class AccountSettingsUiState(
    val account: Account? = null,
    val loading: Boolean = false,
    val error: UiStateError? = null
)

class AccountSettingsViewModel(
    private val accountRepo: AccountRepository
):ViewModel() {
    val uiState = mutableStateOf(AccountSettingsUiState())

    fun collectAccount(){
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.account.collect{
                uiState.value = uiState.value.copy(account = it)
            }
        }
    }

    fun modify(id: String, email: String, address: Address, paymentMethod: PaymentMethod?){
        uiState.value = uiState.value.copy(loading = true)
        viewModelScope.launch(Dispatchers.IO){
            accountRepo.update(Account(id, email, address, paymentMethod = paymentMethod))
                .onFailure{
                    uiState.value = uiState.value.copy(error = UiStateError(it))
                }
        }.invokeOnCompletion {
            uiState.value = uiState.value.copy(loading = false)
        }
    }


}