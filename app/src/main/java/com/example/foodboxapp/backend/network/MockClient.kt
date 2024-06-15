package com.example.foodboxapp.backend.network

import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.data_holders.Store
import com.example.foodboxapp.backend.data_holders.sampleAddress
import com.example.foodboxapp.backend.repositories.dummyProductLists
import com.example.foodboxapp.backend.repositories.dummyStoreList
import kotlinx.coroutines.delay

class MockClient {
    suspend fun getAccount(): Account?{
        delay(1000)
        return Account(
            "example@example.com",
            sampleAddress
        )
    }
    suspend fun getStores(): List<Store>?{
        delay(1000)
        return dummyStoreList
    }
    suspend fun getProducts(storeId: Int): List<Product>?{
        delay(1000)
        return dummyStoreList.firstOrNull{
            it.id == storeId
        }?.let {
            dummyProductLists[it.title]
        }
    }
    suspend fun getAvailableOrders(): List<Order>?{
        delay(1000)
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
    suspend fun getAcceptedOrders(accountId: Int): List<Order>?{
        delay(1000)
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
    suspend fun getOrder(id: Int): Order?{
        delay(1000)
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

        val orders = listOf(
            Order(
                items,
                1,
                sampleAddress,
                listOf(dummyStoreList[0], dummyStoreList[1]),
                items.sumOf { it.totalPrice.toDouble() }.toFloat()
            )
        )
        return orders.firstOrNull{
            it.id == id
        }
    }

}