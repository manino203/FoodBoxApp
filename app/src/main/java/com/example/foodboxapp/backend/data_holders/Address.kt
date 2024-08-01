package com.example.foodboxapp.backend.data_holders

import kotlinx.serialization.Serializable

@Serializable
data class Address (
    val street: String = "",
    val city: String = "",
    val zipCode: String = "",
    val country: String = ""
){
    companion object{
        fun fromMap(data: Any?): Address{
            return (data as? Map<*, *>)?.let{
                Address(
                    street = it["street"].toString(),
                    city = it["city"].toString(),
                    zipCode = it["zipCode"].toString(),
                    country = it["country"].toString()
                )
            } ?: Address()
        }
    }
}
