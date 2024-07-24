package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_sources.AcceptedOrdersDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.ExperimentalSerializationApi

interface AcceptedOrdersRepository {
    val orders: StateFlow<List<Order>>


    suspend fun fetch(uid: String): List<Order>
    suspend fun completeOrder(order: Order)
}

class AcceptedOrdersRepositoryImpl(
    private val dataSource: AcceptedOrdersDataSource
): AcceptedOrdersRepository {
    override val orders: StateFlow<List<Order>>
        get() = _orders.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    override suspend fun fetch(uid: String): List<Order> {
        return dataSource.fetch(uid).also {
            _orders.update { it }
        }
    }

    override suspend fun completeOrder(order: Order) {
        dataSource.completeOrder(order.id)
    }

}