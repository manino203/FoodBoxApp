package com.example.foodboxapp.backend.data_holders

import com.example.foodboxapp.backend.repositories.Store

data class Order(
    val items: List<CartItem>,
    val id: Int = 1,
    val address: Address,
    val stores: List<Store>,
    val total: Float = items.sumOf { it.totalPrice.toDouble() }.toFloat()
)