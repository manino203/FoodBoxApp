package com.example.foodboxapp.viewmodels.worker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.repositories.AcceptedOrdersRepository
import com.example.foodboxapp.backend.repositories.AvailableOrdersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class AvailableOrdersUiState(
    val loading: Boolean = false,
    val orders: List<Order> = emptyList()
)

class AvailableOrdersViewModel(
    private val availableOrdersRepo: AvailableOrdersRepository,
    private val acceptedOrdersRepository: AcceptedOrdersRepository
): ViewModel() {
    val uiState = mutableStateOf(AvailableOrdersUiState())

    fun acceptOrder(order: Order){
        viewModelScope.launch(Dispatchers.IO) {
            acceptedOrdersRepository.addOrder(order)
        }
    }

    fun fetchOrders(){
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = uiState.value.copy(orders = availableOrdersRepo.fetch())
        }
    }

}