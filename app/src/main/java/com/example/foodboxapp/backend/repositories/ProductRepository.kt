package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.data_sources.ProductDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


interface ProductRepository {
    val products: StateFlow<List<Product>>

    suspend fun fetchProducts(storeId: String)

}

class ProductRepositoryImpl(
    private val dataSource: ProductDataSource
): ProductRepository {

    private val _products = MutableStateFlow(emptyList<Product>())

    override val products: StateFlow<List<Product>>
        get() = _products.asStateFlow()

    override suspend fun fetchProducts(storeId: String) {
        _products.update{
            dataSource.fetchProducts(storeId)
       }
    }

}

val dummyProductLists  = mapOf(
    Pair("Tesco",
    listOf(
        Product(
            id = "rqpVJJ2hguBkrtAg2Fum",
            storeId = "sX8U57gwWT9xsUUx7NH7",
            "Tesco Finest Clementine Or Sweet Easy Peeler 600G",
            "https://digitalcontent.api.tesco.com/v2/media/ghs/cfb3c09d-511a-4982-b329-f0d1793ad5e0/ac881f1a-3e3d-4c5f-a316-68da6226a708.jpeg?h=225&w=225",
            2.5f
        ),
        ))
)