package com.example.foodboxapp.backend

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Product(
    val title: String,
    val image: String,
    val price: Float
)


interface ProductRepository {
    val products: StateFlow<List<Product>>

    suspend fun fetchProducts(storeId: String)

}

class ProductRepositoryImpl(
    private val dataSource: ProductDataSource
): ProductRepository{

    private val _products = MutableStateFlow(emptyList<Product>())

    override val products: StateFlow<List<Product>>
        get() = _products.asStateFlow()

    override suspend fun fetchProducts(storeId: String) {
        dataSource.fetchProducts(storeId).onSuccess {products ->
            _products.update{
                products
            }
        }
    }

}

val dummyProductLists = mapOf(
    Pair(
        "Tesco",
        listOf(
            Product(
                "Tesco Finest Clementine Or Sweet Easy Peeler 600G",
                "https://digitalcontent.api.tesco.com/v2/media/ghs/cfb3c09d-511a-4982-b329-f0d1793ad5e0/ac881f1a-3e3d-4c5f-a316-68da6226a708.jpeg?h=225&w=225",
                2.5f
            ),Product(
                "Tesco Granny Smith Apple Minimum 5 Pack",
                "https://digitalcontent.api.tesco.com/v2/media/ghs/a38d07da-970d-4b10-aadf-048b02b5c18f/3493c7a0-1035-4390-9244-77cdb6e7bac0.jpeg?h=225&w=225",
                1.7f
            ),Product(
                "Jaffa Orange Pack",
                "https://digitalcontent.api.tesco.com/v2/media/ghs/a7d897fa-9985-4a48-8ee6-3713ee259014/143aae83-78de-4dce-9ba7-c27d13d3858b_1593483018.jpeg?h=225&w=225",
                2f
            ),Product(
                "Tesco Strawberries 400G",
                "https://digitalcontent.api.tesco.com/v2/media/ghs/8dac8639-165d-439e-8392-f35fc03fe817/1822f26e-4123-49e9-86f5-41090fc381bc.jpeg?h=225&w=225",
                2.5f
            ),

            )
    ),Pair(
        "Billa",
        listOf(
            Product(
                "Tesco Finest Clementine Or Sweet Easy Peeler 600G",
                "https://digitalcontent.api.tesco.com/v2/media/ghs/cfb3c09d-511a-4982-b329-f0d1793ad5e0/ac881f1a-3e3d-4c5f-a316-68da6226a708.jpeg?h=225&w=225",
                2.5f
            )
        )
    ),Pair(
        "Lidl",
        listOf(
            Product(
                "Tesco Finest Clementine Or Sweet Easy Peeler 600G",
                "https://digitalcontent.api.tesco.com/v2/media/ghs/cfb3c09d-511a-4982-b329-f0d1793ad5e0/ac881f1a-3e3d-4c5f-a316-68da6226a708.jpeg?h=225&w=225",
                2.5f
            )
        )
    ),Pair(
        "Kaufland",
        listOf(
            Product(
                "Tesco Finest Clementine Or Sweet Easy Peeler 600G",
                "https://digitalcontent.api.tesco.com/v2/media/ghs/cfb3c09d-511a-4982-b329-f0d1793ad5e0/ac881f1a-3e3d-4c5f-a316-68da6226a708.jpeg?h=225&w=225",
                2.5f
            )
        )
    ),
)