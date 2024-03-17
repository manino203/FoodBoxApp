package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


data class ToolbarUiState(
    val loading: Boolean = false,
    val title: String = "FoodBox"
    )
class ToolbarViewModel: ViewModel() {
    val uiState = mutableStateOf(ToolbarUiState())

    fun updateTitle(newTitle: String){
        uiState.value = uiState.value.copy(title = newTitle)
    }
    fun updateLoading(loading: Boolean){
        uiState.value = uiState.value.copy(loading = loading)
    }
}