package com.example.foodboxapp.backend.data_holders

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val title: String,
    val image: String,
    val price: Float
)