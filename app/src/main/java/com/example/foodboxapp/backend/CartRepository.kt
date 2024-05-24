package com.example.foodboxapp.backend

import com.example.foodboxapp.viewmodels.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface CartRepository {

    val cartItems: StateFlow<List<CartItem>>
    suspend fun attemptRetrieveCartItems(): Result<List<CartItem>>
    suspend fun saveCartItemsPersistent()
    suspend fun saveCartItems(items: List<CartItem>)
    suspend fun addCartItem(item: CartItem)
    suspend fun deleteCartItem(item: CartItem)

}

class CartRepositoryImpl() : CartRepository {

    private val _cartItems = MutableStateFlow(emptyList<CartItem>())
    override val cartItems: StateFlow<List<CartItem>>
        get() = _cartItems.asStateFlow()

    override suspend fun attemptRetrieveCartItems(): Result<List<CartItem>> {
        return Result.failure(NotImplementedError())
    }

    override suspend fun saveCartItemsPersistent() {

    }

    override suspend fun saveCartItems(items: List<CartItem>) {
        _cartItems.update {
            items
        }
        saveCartItemsPersistent()
    }

    override suspend fun addCartItem(item: CartItem) {
        saveCartItems(_cartItems.value.indexOfFirst {
            it.product == item.product
        }.takeIf {
            it != -1
        }?.let { index ->
            _cartItems.value.toMutableList().also {
                it[index] =
                    it[index].copy(quantity = it[index].quantity + item.quantity)
            }
        } ?: _cartItems.value.toMutableList().apply { add(item) })
    }

    override suspend fun deleteCartItem(item: CartItem) {
        _cartItems.value.indexOfFirst {
            it.product == item.product
        }.takeIf {
            it != -1
        }?.let{
            saveCartItems(
                _cartItems.value.toMutableList().apply{ removeAt(it) }
            )
        }
    }

}
