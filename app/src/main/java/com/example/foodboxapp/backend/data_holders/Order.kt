package com.example.foodboxapp.backend.data_holders

import com.example.foodboxapp.backend.repositories.CartItem

data class Order(
    val items: List<CartItem>,
    val account: Account,
    val address: Address = account.address,
    val total: Float = items.sumOf { it.totalPrice.toDouble() }.toFloat()
)