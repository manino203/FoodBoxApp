package com.example.foodboxapp.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed interface ScreenDestination: Parcelable {
    @Parcelize
    data object Splash: ScreenDestination

    @Parcelize
    data class Login(val username: String): ScreenDestination

    @Parcelize
    data object Registration: ScreenDestination

    @Parcelize
    data object Main: ScreenDestination

    @Parcelize
    data class Store(val storeId: String): ScreenDestination

    @Parcelize
    data object Cart: ScreenDestination

    @Parcelize
    data object Settings: ScreenDestination

    @Parcelize
    data object Checkout: ScreenDestination

    @Parcelize
    data object OrderSent: ScreenDestination

    @Parcelize
    data object AccountSettings: ScreenDestination

    @Parcelize
    data object AvailableOrders: ScreenDestination

    @Parcelize
    data object AcceptedOrders: ScreenDestination

    @Parcelize
    data class Order(val orderId: String): ScreenDestination
}