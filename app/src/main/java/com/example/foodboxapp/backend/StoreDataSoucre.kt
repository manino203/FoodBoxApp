package com.example.foodboxapp.backend

import android.accounts.NetworkErrorException
import kotlinx.coroutines.delay

interface StoreDataSource {
    suspend fun fetchStores(): Result<List<Store>>
}

class StoreDataSourceImpl: StoreDataSource{
    override suspend fun fetchStores(): Result<List<Store>> {
        delay(250)
        return Result.success(dummyStoreList)
    }

}