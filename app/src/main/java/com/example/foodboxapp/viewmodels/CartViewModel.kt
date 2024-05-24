package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.CartRepository
import com.example.foodboxapp.backend.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class CartItem(
    val product: Product,
    val quantity: Int,
    val totalPrice: Float
)
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
    fun changeQuantity(product: Product, changeBy: Int){
        viewModelScope.launch(Dispatchers.IO){
            cartRepo.saveCartItems(
                uiState.value.items.let { itemList ->
                    itemList.toMutableList().also { list ->
                        list.indexOfFirst { product == it.product }.also { index ->
                            list[index].also { item ->
                                if (item.quantity + changeBy > 0) {
                                    list[index] = item.copy(
                                        quantity = item.quantity + changeBy,
                                        totalPrice = item.product.price * (item.quantity + changeBy)
                                    )
                                } else {
                                    list.removeAt(index)
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    fun deleteItem(item: CartItem){
        viewModelScope.launch(Dispatchers.IO){
            cartRepo.deleteCartItem(item)
        }
    }

}