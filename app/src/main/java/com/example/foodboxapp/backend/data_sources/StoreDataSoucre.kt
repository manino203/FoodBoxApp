package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Store
import com.example.foodboxapp.backend.repositories.dummyStoreList
import kotlinx.coroutines.delay

interface StoreDataSource {
    suspend fun fetchStores(): Result<List<Store>>
}

class StoreDataSourceImpl: StoreDataSource {
    override suspend fun fetchStores(): Result<List<Store>> {
        delay(250)
        return Result.success(dummyStoreList)
    }

}