package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_sources.OrderDataSource

interface OrderRepository {
    suspend fun sendOrder(order: Order): Result<Unit>
}

class OrderRepositoryImpl(
    private val dataSource: OrderDataSource
) : OrderRepository {
    override suspend fun sendOrder(order: Order): Result<Unit> {
        return dataSource.sendOrder(order)
    }
}