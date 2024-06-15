package com.example.foodboxapp.backend.network

sealed class HttpRoutes(val route: String) {
    data object Account: HttpRoutes("account/")
    data object Stores: HttpRoutes("stores/")
    class Products(val id: Int): HttpRoutes("products/")
    data object AvailableOrders: HttpRoutes("available_orders/")
    class AcceptedOrders(val id: Int): HttpRoutes("accepted_orders/")
    class Order(val id: Int): HttpRoutes("order/")
}
