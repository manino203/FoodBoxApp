package com.example.foodboxapp.viewmodels.worker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.backend.repositories.AvailableOrdersRepository
import com.example.foodboxapp.ui.composables.UiStateError
import com.example.foodboxapp.viewmodels.onFailureWithContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class AvailableOrdersUiState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val account: Account? = null,
    val orders: List<Order> = emptyList(),
    val error: UiStateError? = null
)

class AvailableOrdersViewModel(
    private val availableOrdersRepo: AvailableOrdersRepository,
    private val accountRepo: AccountRepository
): ViewModel() {
    val uiState = mutableStateOf(AvailableOrdersUiState())

    fun acceptOrder(order: Order){
        viewModelScope.launch(IO) {
            uiState.value.account?.id?.let { id ->
                availableOrdersRepo.acceptOrder(order, id).onFailureWithContext{
                    uiState.value = uiState.value.copy(error = UiStateError(it))
                }
            }
        }
    }

    fun refresh(){
        uiState.value = uiState.value.copy(refreshing = true)
        update {
            uiState.value = uiState.value.copy(refreshing = false)
        }
    }
    fun collectChanges(){
        viewModelScope.launch(Main){
            accountRepo.account.collect {
                uiState.value = uiState.value.copy(account = it)
            }
        }
        viewModelScope.launch(Main) {
            availableOrdersRepo.orders.collectLatest {
                uiState.value = uiState.value.copy(orders = it)
            }
        }
        update()
    }

    private fun update(onComplete: () -> Unit = {}){
        uiState.value = uiState.value.copy(loading = true)
        viewModelScope.launch(IO) {
            availableOrdersRepo.fetch().onFailureWithContext{
                uiState.value = uiState.value.copy(error = UiStateError(it))
            }
            withContext(Main){
                uiState.value = uiState.value.copy(loading = false)
                onComplete()
            }
        }
    }
}