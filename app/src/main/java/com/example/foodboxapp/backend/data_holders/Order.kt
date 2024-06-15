package com.example.foodboxapp.backend.data_holders

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val items: List<CartItem>,
    val id: Int = 1,
    val address: Address,
    val stores: List<Store>,
    val total: Float = items.sumOf { it.totalPrice.toDouble() }.toFloat()
)