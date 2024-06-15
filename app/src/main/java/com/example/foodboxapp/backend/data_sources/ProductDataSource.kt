package com.example.foodboxapp.backend.data_sources

import android.accounts.NetworkErrorException
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.repositories.dummyProductLists
import kotlinx.coroutines.delay

interface ProductDataSource {
    suspend fun fetchProducts(storeId: String): Result<List<Product>>
}

class ProductDataSourceImpl: ProductDataSource {
    override suspend fun fetchProducts(storeId: String): Result<List<Product>> {
        delay(250)
        return dummyProductLists[storeId]?.let{
             Result.success(it)
        } ?: Result.failure(NetworkErrorException())

    }

}