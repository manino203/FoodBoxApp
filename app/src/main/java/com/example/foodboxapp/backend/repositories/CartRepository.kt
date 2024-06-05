package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_sources.CartDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface CartRepository {

    val cartItems: StateFlow<List<CartItem>>
    suspend fun retrieveCartItems()
    suspend fun saveCartItemsPersistent()
    suspend fun updateCartItems(items: List<CartItem>)
    suspend fun addCartItem(item: CartItem)
    suspend fun changeItemQuantity(item: CartItem, count: Int)
    suspend fun deleteCartItem(item: CartItem)

    suspend fun clear()

}

class CartRepositoryImpl(
    private val dataSource: CartDataSource
) : CartRepository {

    private val _cartItems = MutableStateFlow(emptyList<CartItem>())
    override val cartItems: StateFlow<List<CartItem>>
        get() = _cartItems.asStateFlow()

    override suspend fun retrieveCartItems() {
        _cartItems.update {
            dataSource.load()
        }
    }

    override suspend fun saveCartItemsPersistent() {
        dataSource.save(cartItems.value)
    }

    override suspend fun updateCartItems(items: List<CartItem>) {
        _cartItems.update {
            items
        }
        saveCartItemsPersistent()
    }

    override suspend fun addCartItem(item: CartItem) {
        updateCartItems(_cartItems.value.indexOfFirst {
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

    override suspend fun changeItemQuantity(item: CartItem, count: Int) {
        updateCartItems(_cartItems.value.toMutableList().also {
            _cartItems.value.indexOf(item).let{index ->
                if(count > 0){
                    it[index] =
                        it[index].copy(quantity = count)
                }else{
                    it.removeAt(index)
                }
            }
        })
    }

    override suspend fun deleteCartItem(item: CartItem) {
        _cartItems.value.indexOfFirst {
            it.product == item.product
        }.takeIf {
            it != -1
        }?.let{
            updateCartItems(
                _cartItems.value.toMutableList().apply{ removeAt(it) }
            )
        }
    }

    override suspend fun clear() {
        updateCartItems(emptyList())
    }

}
