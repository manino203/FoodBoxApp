package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_sources.AvailableOrdersDataSource

interface AvailableOrdersRepository {
    suspend fun fetch(): List<Order>
    suspend fun remove(order: Order)
}

class AvailableOrdersRepositoryImpl(
    private val dataSource: AvailableOrdersDataSource
): AvailableOrdersRepository {
    override suspend fun fetch(): List<Order> = dataSource.fetch()
    override suspend fun remove(order: Order) {
        dataSource.remove(order)
    }


}