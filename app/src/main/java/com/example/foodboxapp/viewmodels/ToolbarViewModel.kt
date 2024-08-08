package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.AccountType
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.backend.repositories.CartRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class ToolbarUiState(
    val visible: Boolean = true,
    val loading: Boolean = false,
    val title: String = "",
    val showActions: Boolean = false,
    val cartItemCount: Int = 0
)
class ToolbarViewModel(
    private val cartRepo: CartRepository,
    private val accountRepo: AccountRepository
): ViewModel() {
    val uiState = mutableStateOf(ToolbarUiState())

    fun updateTitle(newTitle: String){
        uiState.value = uiState.value.copy(title = newTitle)
    }
    fun updateLoading(loading: Boolean){
        uiState.value = uiState.value.copy(loading = loading)
    }
    fun updateVisibility(visible: Boolean){
        uiState.value = uiState.value.copy(visible = visible)
    }

    fun collectCart(){
        viewModelScope.launch(Main){
            accountRepo.account.collect {
                it?.id?.let { id ->
                    withContext(IO){
                        cartRepo.retrieveCartItems(id)
                    }
                }
                uiState.value = uiState.value.copy(showActions = it?.type == AccountType.Client)
            }
        }
        viewModelScope.launch(Main) {
            cartRepo.cartItems.collect {
                uiState.value = uiState.value.copy(cartItemCount = it.size)
            }
        }
    }
}