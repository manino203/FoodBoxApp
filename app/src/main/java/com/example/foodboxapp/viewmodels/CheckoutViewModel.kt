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
import com.example.foodboxapp.ui.composables.UiStateError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class CheckoutUiState(
    val loading: Boolean = false,
    val cartItems: List<CartItem> = emptyList(),
    val account: Account? = null,
    val paymentMethod: PaymentMethod? = account?.paymentMethod,
    val total: Float = cartItems.sumOf { it.totalPrice.toDouble() }.toFloat(),
    val error: UiStateError? = null
)

class CheckoutViewModel(
    private val orderRepo: OrderRepository,
    private val cartRepo: CartRepository,
    private val accountRepository: AccountRepository
): ViewModel() {
    val uiState = mutableStateOf(CheckoutUiState())

    fun loadAccount(){
        viewModelScope.launch(Main) {
            accountRepository.account.collect{
                uiState.value = uiState.value.copy(account = it, paymentMethod = it?.paymentMethod)
            }
        }
    }
    fun sendOrder(order: Order, navigateToOrderSent: () -> Unit){
        uiState.value = uiState.value.copy(loading = true)
        viewModelScope.launch(IO) {
            orderRepo.sendOrder(order)
                .onSuccessWithContext {
                    uiState.value.account?.id?.let { cartRepo.clear(it) }
                    navigateToOrderSent()
                }
                .onFailureWithContext {
                uiState.value = uiState.value.copy(error = UiStateError(it))
            }
            withContext(Main){
                uiState.value = uiState.value.copy(loading = false)
            }
        }
    }

    fun updatePaymentMethod(method: PaymentMethod){
        uiState.value = uiState.value.copy(paymentMethod = method)
    }
    fun loadCartItems(){
        viewModelScope.launch(Main) {
            cartRepo.cartItems.collect{ items ->
                uiState.value = uiState.value.copy(cartItems = items, total = items.sumOf { it.totalPrice.toDouble() }.toFloat())
            }
        }
    }

}