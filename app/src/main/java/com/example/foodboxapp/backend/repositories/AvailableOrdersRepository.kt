package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_sources.AvailableOrdersDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface AvailableOrdersRepository {
    val orders: StateFlow<List<Order>>
    suspend fun fetch()
    suspend fun acceptOrder(order: Order, uid: String)
}

class AvailableOrdersRepositoryImpl(
    private val dataSource: AvailableOrdersDataSource
): AvailableOrdersRepository {
    override val orders: StateFlow<List<Order>>
        get() = _orders.asStateFlow()

    private val _orders = MutableStateFlow(emptyList<Order>())

    override suspend fun fetch() {
        _orders.update{ dataSource.fetch() }
    }
    override suspend fun acceptOrder(order: Order, uid: String) {
        dataSource.acceptOrder(order, uid).also {
            _orders.update {
                it.toMutableList().apply { remove(order) }
            }
        }
    }


}