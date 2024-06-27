package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.backend.repositories.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val loading: Boolean = false,
    val totalPrice: Float = 0f
)
class CartViewModel(
    private val cartRepo: CartRepository,
    private val accountRepo: AccountRepository
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
        viewModelScope.launch(Dispatchers.IO) {
            cartRepo.cartItems.collect{
                updateCart(it)
            }
        }
    }

    fun changeQuantity(product: Product, count: Int){
        viewModelScope.launch(Dispatchers.IO){
            uiState.value.items.find { it.product == product }?.let {
                accountRepo.account.value?.id?.let { id ->
                    cartRepo.changeItemQuantity(it, count, id)
                }
            }
        }
    }

    fun deleteItem(item: CartItem){
        viewModelScope.launch(Dispatchers.IO){
            accountRepo.account.value?.id?.let { cartRepo.deleteCartItem(item, it) }
        }
    }

}