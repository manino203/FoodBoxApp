package com.example.foodboxapp.navigation

sealed interface ScreenDestination {
    data class Login(val username: String): ScreenDestination
    data object Registration: ScreenDestination
    data object Main: ScreenDestination
}