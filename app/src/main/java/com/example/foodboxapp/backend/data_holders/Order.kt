package com.example.foodboxapp.backend.data_holders


data class Order(
    val items: List<CartItem>,
    val id: String,
    val address: Address,
    val stores: List<Store>,
    val total: Float = items.sumOf { it.totalPrice.toDouble() }.toFloat()
)