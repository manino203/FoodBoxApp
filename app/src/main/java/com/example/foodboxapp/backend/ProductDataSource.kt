package com.example.foodboxapp.backend

import android.accounts.NetworkErrorException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update

interface ProductDataSource {
    suspend fun fetchProducts(storeId: String): Result<List<Product>>
}

class ProductDataSourceImpl: ProductDataSource{
    override suspend fun fetchProducts(storeId: String): Result<List<Product>> {
        delay(250)
        return dummyProductLists[storeId]?.let{
             Result.success(it)
        } ?: Result.failure(NetworkErrorException())

    }

}