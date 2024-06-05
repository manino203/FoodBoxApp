package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Order

interface AcceptedOrdersRepository {
    suspend fun fetch(): List<Order>
}

class AcceptedOrdersRepositoryImpl(
    private val dataSource: AcceptedOrdersRepository
): AcceptedOrdersRepository {
    override suspend fun fetch(): List<Order> {
        return dataSource.fetch()
    }

}