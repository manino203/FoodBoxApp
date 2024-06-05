package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.repositories.CartRepository
import com.example.foodboxapp.backend.repositories.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val loading: Boolean = false,
    val totalPrice: Float = 0f
)
class CartViewModel(
    private val cartRepo: CartRepository
):ViewModel() {
    val uiState = mutableStateOf(CartUiState())
    private fun updateCart(items: List<CartItem>){
        uiState.value = CartUiState(
            items,
            false,
            countCartTotal(items)
        )
    }

    private fun countCartTotal(items: List<CartItem>): Float{
        return items.sumOf {
            it.totalPrice.toDouble()
        }.toFloat()
    }


    fun collectCartChanges(){
        viewModelScope.launch(Dispatchers.Default) {
            cartRepo.cartItems.collect{
                updateCart(it)
            }
        }
    }
    fun changeQuantity(product: Product, count: Int){
        viewModelScope.launch(Dispatchers.IO){
            uiState.value.items.find { it.product == product }?.let {
                cartRepo.changeItemQuantity(it, count)
            }
        }
    }

    fun deleteItem(item: CartItem){
        viewModelScope.launch(Dispatchers.IO){
            cartRepo.deleteCartItem(item)
        }
    }

}