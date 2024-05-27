package com.example.foodboxapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.SettingsRepository
import com.example.foodboxapp.backend.SettingsRepositoryImpl.Companion.LANGUAGE_CHOICES
import com.example.foodboxapp.backend.SettingsRepositoryImpl.Companion.THEME_CHOICES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepo: SettingsRepository
): ViewModel() {

    val uiState = mutableStateOf(settingsRepo.state.value)

    fun collectChanges(){
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepo.state.collect{
                uiState.value = it
            }
        }
    }

    fun setLanguage(index: Int){
        settingsRepo.update(uiState.value.copy(language = LANGUAGE_CHOICES[index]))
    }

    fun setTheme(index: Int){
        settingsRepo.update(uiState.value.copy(theme = THEME_CHOICES[index]))

    }

}