package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Store
import com.example.foodboxapp.backend.data_sources.StoreDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface StoreRepository{

    val stores: StateFlow<List<Store>>
    suspend fun fetchStores(): Result<List<Store>>
    fun getStore(id: String): Result<Store>
}


class StoreRepositoryImpl(
    private val dataSource: StoreDataSource
): StoreRepository {
    private val _stores = MutableStateFlow(emptyList<Store>())
    override val stores: StateFlow<List<Store>>
        get() = _stores.asStateFlow()
    override suspend fun fetchStores(): Result<List<Store>> {
        return dataSource.fetchStores() .onSuccess{ s ->
            _stores.update { s }
        }
    }

    override fun getStore(id: String): Result<Store> {
        return stores.value.firstOrNull {
            it.id == id
        }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Store with id: <$id> is not in the list"))
    }


}