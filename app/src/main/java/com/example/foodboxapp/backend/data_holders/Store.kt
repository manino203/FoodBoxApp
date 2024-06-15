package com.example.foodboxapp.backend.data_holders

import kotlinx.serialization.Serializable

@Serializable
data class Store(
    val image: String,
    val address: String,
    val title: String,
    val id: Int
)