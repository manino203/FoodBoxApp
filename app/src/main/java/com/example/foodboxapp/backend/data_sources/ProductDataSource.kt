package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.network.FoodBoxService

interface ProductDataSource {
    suspend fun fetchProducts(storeId: String): Result<List<Product>>
}

class ProductDataSourceImpl(
    private val service: FoodBoxService
): ProductDataSource {
    override suspend fun fetchProducts(storeId: String) = service.fetchProducts(storeId)

}