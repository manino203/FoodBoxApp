package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.backend.repositories.CartItem
import com.example.foodboxapp.backend.repositories.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
sealed class PaymentMethod{
    open val image: Int = R.drawable.mastercard_logo
    open val title: Int = R.string.credit_card
    companion object{
        fun methods() = listOf(Card, GooglePay, Paypal)
    }
    @Serializable
    data object Card: PaymentMethod(){
        override val image = R.drawable.mastercard_logo
        override val title = R.string.credit_card

    }
    @Serializable
    data object GooglePay: PaymentMethod(){
        override val image = R.drawable.google_pay_logo
        override val title = R.string.google_pay

    }
    @Serializable
    data object Paypal: PaymentMethod(){
        override val image = R.drawable.paypal_logo
        override val title = R.string.paypal

    }
}
data class CheckoutUiState(
    val loading: Boolean = false,
    val cartItems: List<CartItem> = emptyList(),
    val account: Account? = null,
    val paymentMethod: PaymentMethod? = account?.paymentMethod,
    val total: Float = cartItems.sumOf { it.totalPrice.toDouble() }.toFloat()
)

class CheckoutViewModel(
    private val cartRepo: CartRepository,
    private val accountRepository: AccountRepository
): ViewModel() {
    val uiState = mutableStateOf(CheckoutUiState())

    fun loadAccount(){
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.account.collect{
                uiState.value = uiState.value.copy(account = it)
            }
        }
    }
    fun sendOrder(order: Order, navigateToOrderSent: () -> Unit){
        viewModelScope.launch(Dispatchers.Default) {
            uiState.value = uiState.value.copy(loading = true)
            delay(500)
            cartRepo.clear()
            uiState.value = uiState.value.copy(loading = false)
        }.invokeOnCompletion { navigateToOrderSent() }
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