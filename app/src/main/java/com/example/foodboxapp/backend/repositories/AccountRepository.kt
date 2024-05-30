package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_sources.AccountDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface AccountRepository {
    val account: StateFlow<Account?>

    suspend fun update(account: Account?)
    suspend fun load()
    suspend fun clear()
}

class AccountRepositoryImpl(
    private val dataSource: AccountDataSource
) : AccountRepository {
    override val account: StateFlow<Account?>
        get() = _account.asStateFlow()

    private val _account = MutableStateFlow<Account?>(
        null
    )

    override suspend fun update(account: Account?) {
        _account.update {
            account
        }
        dataSource.save(account)
    }

    override suspend fun load() {
        _account.update {
            dataSource.load()
        }
    }

    override suspend fun clear() {
        update(null)
    }


}