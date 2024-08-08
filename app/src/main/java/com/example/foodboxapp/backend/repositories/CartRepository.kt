package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_sources.CartDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface CartRepository {

    val cartItems: StateFlow<List<CartItem>>
    fun retrieveCartItems(userId: String)
    fun addCartItem(item: CartItem, userId: String)
    fun changeItemQuantity(item: CartItem, count: Int, userId: String)
    fun deleteCartItem(item: CartItem, userId: String)
    fun clear(userId: String)

}

class CartRepositoryImpl(
    private val dataSource: CartDataSource
) : CartRepository {

    private val _cartItems = MutableStateFlow(emptyList<CartItem>())
    override val cartItems: StateFlow<List<CartItem>>
        get() = _cartItems.asStateFlow()

    override fun retrieveCartItems(userId: String) {
        _cartItems.update {
            dataSource.load(userId)
        }
    }

    private fun saveCartItemsPersistent(userId: String) {
        dataSource.save(cartItems.value, userId)
    }

    private fun updateCartItems(items: List<CartItem>, userId: String) {
        _cartItems.update {
            items
        }
        saveCartItemsPersistent(userId)
    }

    override fun addCartItem(item: CartItem, userId: String) {
        updateCartItems(_cartItems.value.indexOfFirst {
            it.product == item.product
        }.takeIf {
            it != -1
        }?.let { index ->
            _cartItems.value.toMutableList().also {
                it[index] =
                    it[index].copy(quantity = it[index].count + item.count)
            }
        } ?: _cartItems.value.toMutableList().apply { add(item) }, userId)
    }

    override fun changeItemQuantity(item: CartItem, count: Int, userId: String) {
        updateCartItems(_cartItems.value.toMutableList().also {
            _cartItems.value.indexOf(item).let{index ->
                if(count > 0){
                    it[index] =
                        it[index].copy(quantity = count)
                }else{
                    it.removeAt(index)
                }
            }
        }, userId)
    }

    override fun deleteCartItem(item: CartItem, userId: String) {
        _cartItems.value.indexOfFirst {
            it.product == item.product
        }.takeIf {
            it != -1
        }?.let{
            updateCartItems(
                _cartItems.value.toMutableList().apply{ removeAt(it) },
                userId
            )
        }
    }

    override fun clear(userId: String) {
        updateCartItems(emptyList(), userId)
    }

}
