package com.example.foodboxapp.backend.data_holders

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val product: Product,
    val count: Int,
    val store: Store,
    val totalPrice: Float = product.price * count
){
    fun copy(
        product: Product = this.product,
        quantity: Int = this.count,
        store: Store = this.store
    ): CartItem {
        return CartItem(product, quantity, store, totalPrice = product.price * quantity)
    }
}