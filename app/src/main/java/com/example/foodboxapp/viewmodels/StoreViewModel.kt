package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Store
import com.example.foodboxapp.backend.repositories.StoreRepository
import com.example.foodboxapp.ui.composables.UiStateError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class StoreUiState(
    val loading: Boolean = true,
    val isRefreshing: Boolean = false,
    val storeList: List<Store> = emptyList(),
    val error: UiStateError? = null
)

class StoreViewModel(
    private val storeRepo: StoreRepository
): ViewModel() {
    val uiState = mutableStateOf(StoreUiState())

    fun getStores(){
        viewModelScope.launch(Main) {
            uiState.value = uiState.value.copy(loading = true)
            update()
            uiState.value = uiState.value.copy(loading = false)
        }
    }

    fun collectChanges(){
        viewModelScope.launch(Main){
            storeRepo.stores.collect {
                uiState.value = uiState.value.copy(storeList = it)
            }
        }
    }

    fun refresh(){
        uiState.value = uiState.value.copy(loading = true, isRefreshing = true)
        viewModelScope.launch(IO) {
            update()
            uiState.value = uiState.value.copy(loading = false, isRefreshing = false)
        }
    }

    private suspend fun update(){
        withContext(IO){
            storeRepo.fetchStores().onFailureWithContext {
                uiState.value = uiState.value.copy(error = UiStateError(it))
            }
        }
    }
}