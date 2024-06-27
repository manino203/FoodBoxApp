package com.example.foodboxapp.backend.data_sources

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.foodboxapp.backend.data_holders.CartItem
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


const val CART_PREFS_NAME = "cart"
interface CartDataSource {
    suspend fun save(cartItems: List<CartItem>, userId: String)
    suspend fun load(userId: String): List<CartItem>

}

class CartDataSourceImpl(
    private val preferences: SharedPreferences
): CartDataSource {


    override suspend fun save(cartItems: List<CartItem>, userId: String) {
        preferences.edit {
            putString(userId, Json.encodeToString(cartItems))
            commit()
        }
    }

    override suspend fun load(userId: String): List<CartItem> {
        return Json.decodeFromString(preferences.getString(userId, "[]")?: "[]")
    }

}