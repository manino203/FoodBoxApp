package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodboxapp.backend.data_sources.AccountType


data class NavDrawerUiState(
    val accType: AccountType = AccountType.Client
)

class NavDrawerViewModel: ViewModel() {
    val uiState = mutableStateOf(NavDrawerUiState())
}