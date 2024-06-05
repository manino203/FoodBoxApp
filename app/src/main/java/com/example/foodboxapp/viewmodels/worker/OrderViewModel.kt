package com.example.foodboxapp.viewmodels.worker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.repositories.OrderRepository

data class OrderUiState(
    val loading: Boolean = false,
    val crossedOutItems: List<CartItem> = emptyList()
)

class OrderViewModel(
    private val orderRepo: OrderRepository
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

    }

}