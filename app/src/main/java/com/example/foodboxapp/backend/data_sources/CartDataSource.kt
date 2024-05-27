package com.example.foodboxapp.backend.data_sources

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.foodboxapp.backend.repositories.CartItem
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


const val CART_PREFS_NAME = "cart"
interface CartDataSource {
    suspend fun save(cartItems: List<CartItem>)
    suspend fun load(): List<CartItem>

}

class CartDataSourceImpl(
    private val preferences: SharedPreferences
): CartDataSource {

    companion object{
        private const val CART_ITEMS_KEY = "cartItems"
    }
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun save(cartItems: List<CartItem>) {
        preferences.edit {
            putString(CART_ITEMS_KEY, Json.encodeToString(cartItems))
            commit()
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun load(): List<CartItem> {
        return Json.decodeFromString(preferences.getString(CART_ITEMS_KEY, "[]")?: "[]")
    }

}