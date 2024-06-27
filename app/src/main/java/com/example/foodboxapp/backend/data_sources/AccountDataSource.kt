package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.network.FoodBoxService

interface AccountDataSource {
    suspend fun login(email: String, password: String): Account
    suspend fun register(email: String, password: String): Account
    suspend fun resumeSession(): Account
    suspend fun update(account: Account): Account
    fun logout()
}

class AccountDataSourceImpl(
    private val service: FoodBoxService
): AccountDataSource {

    override suspend fun login(email: String, password: String): Account {
        return service.login(email, password)
    }

    override suspend fun register(email: String, password: String): Account {
        return service.register(email, password) ?: throw Exception("Something went wrong")
    }

    override suspend fun resumeSession(): Account {
        return service.resumeSession()
    }

    override suspend fun update(account: Account): Account {
        return service.updateAccount(account)?: throw Exception("Something went wrong")
    }

    override fun logout(){
        service.logout()
    }
}



