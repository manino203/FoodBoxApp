package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_sources.AcceptedOrdersDataSource
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface AcceptedOrdersRepository {
    suspend fun fetch(): List<Order>
    suspend fun edit(block: (MutableList<Order>) -> Unit)
    suspend fun addOrder(order: Order)
    suspend fun removeOrder(order: Order)

    suspend fun getOrder(id: Int): Result<Order>
}

class AcceptedOrdersRepositoryImpl(
    private val dataSource: AcceptedOrdersDataSource
): AcceptedOrdersRepository {
    override suspend fun fetch(): List<Order> {
        return dataSource.fetch()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun edit(block: (MutableList<Order>) -> Unit) {
        fetch().also {
            dataSource.edit { key ->
                putString(key, Json.encodeToString(value = it.toMutableList().also { block(it) }))
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun addOrder(order: Order) {
        edit {
            it.add(order)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun removeOrder(order: Order) {
        edit{
            it.remove(order)
        }
    }

    override suspend fun getOrder(id: Int): Result<Order> {
        return fetch().firstOrNull {
            it.id == id
        }?.let {
            Result.success(it)
        }?: Result.failure(NoSuchElementException("Order with id: <$id> is not in the list"))
    }


}