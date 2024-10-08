package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_sources.AcceptedOrdersDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface AcceptedOrdersRepository {
    val orders: StateFlow<List<Order>>


    suspend fun fetch(uid: String): Result<List<Order>>
    suspend fun completeOrder(order: Order): Result<Unit>
}

class AcceptedOrdersRepositoryImpl(
    private val dataSource: AcceptedOrdersDataSource
): AcceptedOrdersRepository {
    override val orders: StateFlow<List<Order>>
        get() = _orders.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    override suspend fun fetch(uid: String): Result<List<Order>> {
        return dataSource.fetch(uid).onSuccess { o ->
            _orders.update { o }
        }
    }

    override suspend fun completeOrder(order: Order): Result<Unit> {
        return dataSource.completeOrder(order.id).onSuccess {
            _orders.update {
                it.toMutableList().apply { remove(order) }
            }
        }

    }

}