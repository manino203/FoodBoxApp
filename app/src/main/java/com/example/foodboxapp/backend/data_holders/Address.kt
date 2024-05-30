package com.example.foodboxapp.backend.data_holders

import kotlinx.serialization.Serializable

@Serializable
data class Address (
    val street: String = "",
    val city: String = "",
    val zipCode: String = "",
    val country: String = ""
)

val sampleAddress = Address(
    street = "17.novembra 1296",
    city = "Cadca",
    zipCode = "02201",
    country = "Slovakia"
)