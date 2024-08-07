package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.data_sources.ProductDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


interface ProductRepository {
    val products: StateFlow<List<Product>>

    suspend fun fetchProducts(storeId: String): Result<List<Product>>

}

class ProductRepositoryImpl(
    private val dataSource: ProductDataSource
): ProductRepository {

    private val _products = MutableStateFlow(emptyList<Product>())

    override val products: StateFlow<List<Product>>
        get() = _products.asStateFlow()

    override suspend fun fetchProducts(storeId: String): Result<List<Product>> {
        return dataSource.fetchProducts(storeId).onSuccess { p ->
            _products.update{ p }
        }
    }
}
