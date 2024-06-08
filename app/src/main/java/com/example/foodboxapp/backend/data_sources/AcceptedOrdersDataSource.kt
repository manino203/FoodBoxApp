package com.example.foodboxapp.backend.data_sources

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.foodboxapp.backend.data_holders.Order
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

interface AcceptedOrdersDataSource {
    suspend fun fetch(): List<Order>

    suspend fun edit(update: SharedPreferences.Editor.(String) -> Unit)

    suspend fun clear()
}

class AcceptedOrdersDataSourceImpl(
    private val sharedPrefs: SharedPreferences
): AcceptedOrdersDataSource {

    companion object{
        const val ACCEPTED_ORDERS_PREFS = "accepted_orders"
        private const val ORDERS_KEY = "accepted_orders"
    }
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun fetch(): List<Order> {
        return sharedPrefs.getString(ORDERS_KEY, null)?.let { Json.decodeFromString<List<Order>>(it) } ?: emptyList()
    }

    override suspend fun edit(update: SharedPreferences.Editor.(String) -> Unit) {
        sharedPrefs.edit {
            update(ORDERS_KEY)
            commit()
        }
    }

    override suspend fun clear() {
        edit {
            clear()
            commit()
        }
    }
}