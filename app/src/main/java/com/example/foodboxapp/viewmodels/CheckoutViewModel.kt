package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_holders.PaymentMethod
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.backend.repositories.CartRepository
import com.example.foodboxapp.backend.repositories.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class CheckoutUiState(
    val loading: Boolean = false,
    val cartItems: List<CartItem> = emptyList(),
    val account: Account? = null,
    val paymentMethod: PaymentMethod? = account?.paymentMethod,
    val total: Float = cartItems.sumOf { it.totalPrice.toDouble() }.toFloat()
)

class CheckoutViewModel(
    private val orderRepo: OrderRepository,
    private val cartRepo: CartRepository,
    private val accountRepository: AccountRepository
): ViewModel() {
    val uiState = mutableStateOf(CheckoutUiState())

    fun loadAccount(){
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.account.collect{
                uiState.value = uiState.value.copy(account = it, paymentMethod = it?.paymentMethod)
            }
        }
    }
    fun sendOrder(order: Order, navigateToOrderSent: () -> Unit){
        uiState.value = uiState.value.copy(loading = true)
        viewModelScope.launch(Dispatchers.Default) {
            orderRepo.sendOrder(order)
            uiState.value.account?.id?.let { cartRepo.clear(it) }
        }.invokeOnCompletion {
            uiState.value = uiState.value.copy(loading = false)
            navigateToOrderSent()
        }
    }

    fun updatePaymentMethod(method: PaymentMethod){
        uiState.value = uiState.value.copy(paymentMethod = method)
    }
    fun loadCartItems(){
        viewModelScope.launch(Dispatchers.Default) {
            cartRepo.cartItems.collect{ items ->
                uiState.value = uiState.value.copy(cartItems = items, total = items.sumOf { it.totalPrice.toDouble() }.toFloat())
            }
        }
    }

}