package com.example.foodboxapp.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.AccountType
import com.example.foodboxapp.backend.repositories.AccountRepository
import com.example.foodboxapp.backend.repositories.SessionState
import com.example.foodboxapp.navigation.ScreenDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class MainUiState(
    val loading: Boolean = false,
    val screenDestination: ScreenDestination = ScreenDestination.Splash,
    val account: Account? = null
)

class MainViewModel(
    private val sessionRepo: AccountRepository,
    private val accountRepo: AccountRepository
): ViewModel() {
    val uiState = mutableStateOf(MainUiState())

    fun collectSessionState() {
        Log.d("collect", "asd")
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepo.state.collect {
                Log.d("sessionState change", "$it")
                when (it) {
                    SessionState.NOT_LOADED -> {
                        uiState.value =
                            uiState.value.copy(screenDestination = ScreenDestination.Splash)
                        sessionRepo.resumeSession()
                    }
                    SessionState.SESSION_AVAILABLE -> uiState.value = uiState.value.copy(screenDestination = ScreenDestination.Main)
                    SessionState.NOT_LOGGED_IN -> uiState.value = uiState.value.copy(screenDestination = ScreenDestination.Login(""))
                    SessionState.LOGGED_IN -> {
                        if(accountRepo.account.value?.type == AccountType.Client){
                            uiState.value =
                                uiState.value.copy(screenDestination = ScreenDestination.Main)
                        }else{
                            uiState.value =
                                uiState.value.copy(screenDestination = ScreenDestination.AvailableOrders)
                        }
                    }
                    SessionState.LOGGED_OUT -> uiState.value = uiState.value.copy(screenDestination = ScreenDestination.Login(""))
                    SessionState.SESSION_EXPIRED -> uiState.value = uiState.value.copy(screenDestination = ScreenDestination.Login(""))
                }
            }
        }
    }

    fun logout(){
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepo.logout()
        }
    }
    fun collectAccount(){
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.account.collect{
                uiState.value = uiState.value.copy(account = it)
            }
        }
    }
}