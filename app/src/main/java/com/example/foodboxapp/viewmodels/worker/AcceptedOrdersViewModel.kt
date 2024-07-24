package com.example.foodboxapp.viewmodels.worker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.repositories.AcceptedOrdersRepository
import com.example.foodboxapp.backend.repositories.AccountRepository
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

data class AcceptedOrdersUiState(
    val loading: Boolean = false,
    val orders: List<Order> = emptyList()
)

class AcceptedOrdersViewModel(
    private val repo: AcceptedOrdersRepository,
    private val accountRepo: AccountRepository
): ViewModel(){
    val uiState = mutableStateOf(AcceptedOrdersUiState())

    fun collectChanges(){
        viewModelScope.launch(IO) {
            accountRepo.account.collect{
                it?.id?.let { id ->
                    uiState.value = uiState.value.copy(loading = true)
                    repo.fetch(id)
                    uiState.value = uiState.value.copy(loading = false)
                }
            }
        }
        viewModelScope.launch(Default) {
            repo.orders.collect{
                uiState.value = uiState.value.copy(orders = it)
            }
        }

    }
}