package com.example.foodboxapp.viewmodels.worker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.repositories.AcceptedOrdersRepository
import kotlinx.coroutines.launch

data class AcceptedOrdersUiState(
    val loading: Boolean = false,
    val orders: List<Order> = emptyList()
)

class AcceptedOrdersViewModel(
    private val repo: AcceptedOrdersRepository
): ViewModel(){
    val uiState = mutableStateOf(AcceptedOrdersUiState())

    fun fetchOrders(){
        viewModelScope.launch {
            uiState.value = uiState.value.copy(orders = repo.fetch())
        }
    }

}