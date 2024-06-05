package com.example.foodboxapp.viewmodels.worker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodboxapp.backend.repositories.AcceptedOrdersRepository

data class AcceptedOrdersUiState(
    val loading: Boolean = false
)

class AcceptedOrdersViewModel(
    private val repo: AcceptedOrdersRepository
): ViewModel(){
    val uiState = mutableStateOf(AcceptedOrdersUiState())
}