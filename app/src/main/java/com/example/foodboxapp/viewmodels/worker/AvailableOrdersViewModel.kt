package com.example.foodboxapp.viewmodels.worker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.repositories.AcceptedOrdersRepository
import com.example.foodboxapp.backend.repositories.AvailableOrdersRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class AvailableOrdersUiState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val orders: List<Order> = emptyList()
)

class AvailableOrdersViewModel(
    private val availableOrdersRepo: AvailableOrdersRepository,
    private val acceptedOrdersRepository: AcceptedOrdersRepository
): ViewModel() {
    val uiState = mutableStateOf(AvailableOrdersUiState())

    fun acceptOrder(order: Order){
        viewModelScope.launch(IO) {
            acceptedOrdersRepository.addOrder(order)
            availableOrdersRepo.remove(order)
        }
    }

    fun refresh(){
        uiState.value = uiState.value.copy(refreshing = true)
        update {
            uiState.value = uiState.value.copy(refreshing = false)
        }
    }

    fun update(onComplete: () -> Unit = {}){
        uiState.value = uiState.value.copy(loading = true)
        viewModelScope.launch(IO) {
            uiState.value = uiState.value.copy(orders = availableOrdersRepo.fetch())
        }.invokeOnCompletion {
            uiState.value = uiState.value.copy(loading = false)
            onComplete()
        }
    }
}