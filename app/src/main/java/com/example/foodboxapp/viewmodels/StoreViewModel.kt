package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Store
import com.example.foodboxapp.backend.repositories.StoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class StoreUiState(
    val loading: Boolean = false,
    val storeList: List<Store> = emptyList()
)

class StoreViewModel(
    private val storeRepo: StoreRepository
): ViewModel() {
    val uiState = mutableStateOf(StoreUiState())

    fun getStores(){
        viewModelScope.launch(Dispatchers.Default) {
            storeRepo.fetchStores()
            storeRepo.stores.collect{
                uiState.value = uiState.value.copy(storeList = it)
            }
        }
    }
}