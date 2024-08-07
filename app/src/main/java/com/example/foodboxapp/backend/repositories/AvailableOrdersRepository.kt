package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_sources.AvailableOrdersDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface AvailableOrdersRepository {
    val orders: StateFlow<List<Order>>
    suspend fun fetch(): Result<List<Order>>
    suspend fun acceptOrder(order: Order, uid: String): Result<Unit>
}

class AvailableOrdersRepositoryImpl(
    private val dataSource: AvailableOrdersDataSource
): AvailableOrdersRepository {
    override val orders: StateFlow<List<Order>>
        get() = _orders.asStateFlow()

    private val _orders = MutableStateFlow(emptyList<Order>())

    override suspend fun fetch(): Result<List<Order>> {
        return dataSource.fetch().onSuccess { o ->
            _orders.update{ o }
        }
    }
    override suspend fun acceptOrder(order: Order, uid: String): Result<Unit> {
        return dataSource.acceptOrder(order, uid).onSuccess {
            _orders.update {
                it.toMutableList().apply { remove(order) }
            }
        }
    }


}