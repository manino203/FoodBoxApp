package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.network.FoodBoxService
import kotlinx.serialization.ExperimentalSerializationApi

interface AcceptedOrdersDataSource {
    suspend fun fetch(uid: String): Result<List<Order>>

    suspend fun completeOrder(id: String): Result<Unit>

}

class AcceptedOrdersDataSourceImpl(
    private val service: FoodBoxService
): AcceptedOrdersDataSource {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun fetch(uid: String): Result<List<Order>> {
        return service.fetchAcceptedOrders(uid)
    }

    override suspend fun completeOrder(id: String): Result<Unit> {
        return service.completeOrder(id)
    }


}