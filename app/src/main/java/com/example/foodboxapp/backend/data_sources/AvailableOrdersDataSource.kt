package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_holders.sampleAddress
import com.example.foodboxapp.backend.repositories.dummyProductLists
import com.example.foodboxapp.backend.repositories.dummyStoreList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


interface AvailableOrdersDataSource{
    suspend fun fetch(): List<Order>
    suspend fun remove(order: Order)
}

class AvailableOrdersDataSourceImpl(

): AvailableOrdersDataSource {

    val orders: StateFlow<List<Order>>
        get() = _orders.asStateFlow()
    private val _orders = MutableStateFlow(emptyList<Order>())

    override suspend fun fetch(): List<Order> {
        val items = dummyProductLists["Tesco"]?.map{
            val count = 1
            CartItem(
                it,
                count,
                dummyStoreList[0],
                it.price * count
            )
        }?.toMutableList()?.apply{
            addAll(
                dummyProductLists["Billa"]?.map {
                    val count = 1
                    CartItem(
                        it,
                        count,
                        dummyStoreList[1],
                        it.price * count
                    )
                } ?: emptyList()
            )
        } ?: emptyList()

        return listOf(
            Order(
                items,
                1,
                sampleAddress,
                listOf(dummyStoreList[0], dummyStoreList[1]),
                items.sumOf { it.totalPrice.toDouble() }.toFloat()
            )
        ).also {
            _orders.update {
                it
            }
        }
    }

    override suspend fun remove(order: Order) {
        _orders.update {
            it.toMutableList().apply { remove(order) }
        }
    }


}