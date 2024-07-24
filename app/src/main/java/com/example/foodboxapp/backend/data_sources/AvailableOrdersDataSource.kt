package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.network.FoodBoxService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


interface AvailableOrdersDataSource{
    suspend fun fetch(): List<Order>
    suspend fun acceptOrder(order: Order, uid: String)
}

class AvailableOrdersDataSourceImpl(
    private val service: FoodBoxService
): AvailableOrdersDataSource {

    val orders: StateFlow<List<Order>>
        get() = _orders.asStateFlow()
    private val _orders = MutableStateFlow(emptyList<Order>())

    override suspend fun fetch(): List<Order> {
        return service.fetchAvailableOrders()
        .also {
            _orders.update {
                it
            }
        }
    }

    override suspend fun acceptOrder(order: Order, uid: String) {
        service.acceptOrder(order.id, uid)
        _orders.update {
            it.toMutableList().apply { remove(order) }
        }
    }


}