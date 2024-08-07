package com.example.foodboxapp.viewmodels.worker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.repositories.AcceptedOrdersRepository
import com.example.foodboxapp.ui.composables.UiStateError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class OrderUiState(
    val loading: Boolean = false,
    val order: Order? = null,
    val crossedOutItems: List<CartItem> = emptyList(),
    val error: UiStateError? = null
)

class OrderViewModel(
    private val acceptedOrdersRepo: AcceptedOrdersRepository
) : ViewModel() {
    val uiState = mutableStateOf(OrderUiState())

    fun crossOutItem(item: CartItem) {
        uiState.value = uiState.value.copy(
            crossedOutItems = uiState.value.crossedOutItems.toMutableList().apply {
                if (contains(item)){
                    remove(item)
                }else{
                    add(item)
                }
            })
    }

    fun completeOrder(order: Order, onComplete: () -> Unit){
        uiState.value = uiState.value.copy(loading = true)
        viewModelScope.launch(IO){
            acceptedOrdersRepo.completeOrder(order).onFailure { uiState.value = uiState.value.copy(error = UiStateError(it)) }
        }.invokeOnCompletion {
            uiState.value = uiState.value.copy(loading = false)
            onComplete()
        }
    }

    fun update(orderId: String){
        viewModelScope.launch(IO){
            acceptedOrdersRepo.orders.collectLatest { orders ->
                orders.firstOrNull{it.id == orderId }?.let {
                uiState.value = uiState.value.copy(order = it)
            }
            }
        }
    }
}