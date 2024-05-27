package com.example.foodboxapp.navigation

sealed interface ScreenDestination {
    data object Splash: ScreenDestination
    data class Login(val username: String): ScreenDestination
    data object Registration: ScreenDestination
    data object Main: ScreenDestination
    data class Store(val store: com.example.foodboxapp.backend.repositories.Store): ScreenDestination
    data object Cart: ScreenDestination

    data object Settings: ScreenDestination
}