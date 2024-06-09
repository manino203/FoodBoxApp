package com.example.foodboxapp.viewmodels.worker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.repositories.AcceptedOrdersRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

data class OrderUiState(
    val loading: Boolean = false,
    val order: Order? = null,
    val crossedOutItems: List<CartItem> = emptyList()
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

    fun completeOrder(order: Order){
        viewModelScope.launch(IO){
            acceptedOrdersRepo.removeOrder(order)
        }
    }

    fun update(orderId: Int){
        viewModelScope.launch(IO){
            acceptedOrdersRepo.getOrder(orderId).onSuccess {
                uiState.value = uiState.value.copy(order = it)
            }.onFailure {

            }
        }
    }
}