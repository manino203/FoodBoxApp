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
}

class CartRepositoryImpl(): CartRepository{

    private val _cartItems = MutableStateFlow(dummyCartItems)
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

}

private val dummyCartItems = dummyProductLists["Tesco"]?.map {
    CartItem(
        it,
        1,
        it.price
    )
}?: emptyList()