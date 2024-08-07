package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.network.FoodBoxService


interface AvailableOrdersDataSource{
    suspend fun fetch(): Result<List<Order>>
    suspend fun acceptOrder(order: Order, uid: String): Result<Unit>
}

class AvailableOrdersDataSourceImpl(
    private val service: FoodBoxService
): AvailableOrdersDataSource {


    override suspend fun fetch(): Result<List<Order>> {
        return service.fetchAvailableOrders()
    }

    override suspend fun acceptOrder(order: Order, uid: String): Result<Unit> {
        return service.acceptOrder(order.id, uid)
    }


}