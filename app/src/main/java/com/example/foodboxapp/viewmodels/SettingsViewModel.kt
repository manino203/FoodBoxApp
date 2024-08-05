package com.example.foodboxapp.viewmodels

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.repositories.SettingsRepository
import com.example.foodboxapp.backend.repositories.SettingsRepositoryImpl.Companion.LANGUAGE_CHOICES
import com.example.foodboxapp.backend.repositories.SettingsRepositoryImpl.Companion.THEME_CHOICES
import com.example.foodboxapp.backend.repositories.SettingsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsViewModel(
    private val settingsRepo: SettingsRepository
): ViewModel() {

    val uiState = mutableStateOf(SettingsState())

    fun collectChanges(){
        viewModelScope.launch(Dispatchers.Default) {
            settingsRepo.state.collect{
                uiState.value = it
            }
        }
    }

    fun setLanguage(index: Int, context: Context){
        settingsRepo.update(uiState.value.copy(language = LANGUAGE_CHOICES[index]))
        context.resources.updateConfiguration(Configuration().apply {
                setLocale(Locale("sk"))
            },
            context.resources.displayMetrics
        )
    }

    fun setTheme(index: Int){
        settingsRepo.update(uiState.value.copy(theme = THEME_CHOICES[index]))

    }

}