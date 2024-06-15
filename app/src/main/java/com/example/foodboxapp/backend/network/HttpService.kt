package com.example.foodboxapp.backend.network

import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.data_holders.Store

interface HttpService {
    suspend fun fetchAccount(sessionId: Int): Result<Account>
    suspend fun fetchStores(sessionId: Int): Result<List<Store>>
    suspend fun fetchProducts(storeId: Int, sessionId: Int): Result<List<Product>>
    suspend fun fetchAvailableOrders(sessionId: Int): Result<List<Order>>
    suspend fun fetchAcceptedOrders(accountId: Int, sessionId: Int): Result<List<Order>>
    suspend fun fetchOrder(orderId: Int, sessionId: Int): Result<Order>
}

class HttpServiceImpl(
     private val client: MockClient
): HttpService{


    override suspend fun fetchAccount(sessionId: Int): Result<Account> {
        return client.getAccount()?.let {
            Result.success(it)
        } ?: Result.failure(Exception("fetch failed"))
    }

    override suspend fun fetchStores(sessionId: Int): Result<List<Store>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchProducts(storeId: Int, sessionId: Int): Result<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAvailableOrders(sessionId: Int): Result<List<Order>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAcceptedOrders(accountId: Int, sessionId: Int): Result<List<Order>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchOrder(orderId: Int, sessionId: Int): Result<Order> {
        TODO("Not yet implemented")
    }
}
