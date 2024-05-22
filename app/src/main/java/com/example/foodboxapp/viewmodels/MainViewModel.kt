package com.example.foodboxapp.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.SessionDataSourceImpl
import com.example.foodboxapp.backend.SessionRepository
import com.example.foodboxapp.backend.SessionRepositoryImpl
import com.example.foodboxapp.backend.SessionState
import com.example.foodboxapp.navigation.ScreenDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.Closeable


data class MainUiState(
    val loading: Boolean = false,
    val screenDestination: ScreenDestination = ScreenDestination.Splash
)

class MainViewModel(
    private val sessionRepo: SessionRepository
): ViewModel() {
    val uiState = mutableStateOf(MainUiState())

    fun collectSessionState() {
        Log.d("collect", "asd")
        viewModelScope.launch(Dispatchers.Default) {
            sessionRepo.state.collect {
                Log.d("sessionState change", "$it")
                when (it) {
                    SessionState.NOT_LOADED -> {
                        sessionRepo.resumeSession()
                        uiState.value =
                            uiState.value.copy(screenDestination = ScreenDestination.Splash)
                    }
                    SessionState.SESSION_AVAILABLE -> uiState.value = uiState.value.copy(screenDestination = ScreenDestination.Main)
                    SessionState.NOT_LOGGED_IN -> uiState.value = uiState.value.copy(screenDestination = ScreenDestination.Login(""))
                    SessionState.LOGGED_IN -> uiState.value = uiState.value.copy(screenDestination = ScreenDestination.Main)
                    SessionState.LOGGED_OUT -> uiState.value = uiState.value.copy(screenDestination = ScreenDestination.Login(""))
                    SessionState.SESSION_EXPIRED -> uiState.value = uiState.value.copy(screenDestination = ScreenDestination.Login(""))
                }
            }
        }
    }

    fun logout(){
        sessionRepo.logout()
    }

}