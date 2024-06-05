package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_sources.OrderDataSource

interface OrderRepository {

}

class OrderRepositoryImpl(
    private val dataSource: OrderDataSource
) : OrderRepository {

}