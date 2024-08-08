package com.example.foodboxapp.backend.data_holders

import com.google.firebase.Timestamp


data class Order(
    val items: List<CartItem>,
    val id: String,
    val address: Address,
    val stores: List<Store>,
    val timestamp: Timestamp? = null,
    val total: Float = items.sumOf { it.totalPrice.toDouble() }.toFloat()
)