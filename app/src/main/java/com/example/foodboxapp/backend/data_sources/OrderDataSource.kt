package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.network.FoodBoxService

interface OrderDataSource {
    suspend fun sendOrder(order: Order): Result<Unit>

}
class OrderDataSourceImpl(
    private val service: FoodBoxService
) : OrderDataSource {
    override suspend fun sendOrder(order: Order): Result<Unit> {
        return service.sendOrder(order)
    }
}