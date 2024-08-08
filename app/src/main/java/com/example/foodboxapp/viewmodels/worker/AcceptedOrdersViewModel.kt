package com.example.foodboxapp.viewmodels.worker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.repositories.AcceptedOrdersRepository
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.ui.composables.UiStateError
import com.example.foodboxapp.viewmodels.onFailureWithContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AcceptedOrdersUiState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val orders: List<Order> = emptyList(),
    val userId: String? = null,
    val error: UiStateError? = null
)

class AcceptedOrdersViewModel(
    private val repo: AcceptedOrdersRepository,
    private val accountRepo: AccountRepository
): ViewModel(){
    val uiState = mutableStateOf(AcceptedOrdersUiState())
    fun collectChanges(){
        viewModelScope.launch(Main) {
            accountRepo.account.collect{ acc ->
                acc?.id?.let { id ->
                    uiState.value = uiState.value.copy(userId = id)
                    update(id)
                }
            }
        }
        viewModelScope.launch(Main) {
            repo.orders.collect{
                uiState.value = uiState.value.copy(orders = it)
            }
        }
    }

    fun refresh(){
        uiState.value.userId?.let {
            uiState.value = uiState.value.copy(refreshing = true)
            viewModelScope.launch(Main){
                update(it) {
                    uiState.value = uiState.value.copy(refreshing = false)
                }
            }
        }
    }

    private suspend fun update(id: String, onComplete: () -> Unit = {}){
        uiState.value = uiState.value.copy(loading = true)
        withContext(IO) {
            repo.fetch(id).onFailureWithContext {
                uiState.value = uiState.value.copy(error = UiStateError(it))
            }
        }
        withContext(Main){
            uiState.value = uiState.value.copy(loading = false)
            onComplete()
        }
    }

}