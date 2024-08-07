package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.network.FoodBoxService

interface AccountDataSource {
    suspend fun login(email: String, password: String): Result<Account>
    suspend fun register(email: String, password: String): Result<Account>
    suspend fun resumeSession(): Result<Account>
    suspend fun update(account: Account): Result<Account>
    suspend fun logout(): Result<Unit>
}

class AccountDataSourceImpl(
    private val service: FoodBoxService
): AccountDataSource {

    override suspend fun login(email: String, password: String): Result<Account> {
        return service.login(email, password)
    }

    override suspend fun register(email: String, password: String): Result<Account> {
        return service.register(email, password)
    }

    override suspend fun resumeSession(): Result<Account> {
        return service.resumeSession()
    }

    override suspend fun update(account: Account): Result<Account> {
        return service.updateAccount(account)
    }

    override suspend fun logout(): Result<Unit> {
        return service.logout()
    }
}



