package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Store
import com.example.foodboxapp.backend.network.FoodBoxService

interface StoreDataSource {
    suspend fun fetchStores(): List<Store>
}

class StoreDataSourceImpl(
    private val service: FoodBoxService
): StoreDataSource {
    override suspend fun fetchStores(): List<Store> = service.fetchStores()
}