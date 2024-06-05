package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_holders.sampleAddress
import com.example.foodboxapp.backend.repositories.dummyProductLists
import com.example.foodboxapp.backend.repositories.dummyStoreList


interface AvailableOrdersDataSource{
    fun fetch(): List<Order>
}

class AvailableOrdersDataSourceImpl(

): AvailableOrdersDataSource {
    override fun fetch(): List<Order> {
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
        )
    }
}