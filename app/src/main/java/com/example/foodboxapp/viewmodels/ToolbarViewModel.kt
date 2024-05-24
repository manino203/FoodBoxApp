package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


enum class Action{
    CART,
    HOME
}

data class ToolbarUiState(
    val visible: Boolean = true,
    val loading: Boolean = false,
    val title: String = "",
    val action:Action = Action.CART,
    val cartItemCount: Int = 0
)
class ToolbarViewModel(
    private val cartRepo: CartRepository
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

    fun updateAction(action: Action){
        uiState.value = uiState.value.copy(action = action)
    }

    fun collectCartItemCount(){
        viewModelScope.launch(Dispatchers.Default){
            cartRepo.cartItems.collect {
                uiState.value = uiState.value.copy(cartItemCount = it.size)
            }
        }
    }
}