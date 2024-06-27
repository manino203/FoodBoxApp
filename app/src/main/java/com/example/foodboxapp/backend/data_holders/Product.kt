package com.example.foodboxapp.backend.data_holders

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val storeId: String,
    val title: String,
    val imageUrl: String? = null,
    val price: Float,
    val details: String? = null
)