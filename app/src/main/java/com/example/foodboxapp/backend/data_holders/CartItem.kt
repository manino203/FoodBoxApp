package com.example.foodboxapp.backend.data_holders

import com.example.foodboxapp.backend.repositories.Product
import com.example.foodboxapp.backend.repositories.Store
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val product: Product,
    val quantity: Int,
    val store: Store,
    val totalPrice: Float = product.price * quantity
){
    fun copy(
        product: Product = this.product,
        quantity: Int = this.quantity,
        store: Store = this.store
    ): CartItem {
        return CartItem(product, quantity, store, totalPrice = product.price * quantity)
    }
}