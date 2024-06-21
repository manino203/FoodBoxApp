package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.network.HttpService


interface AccountDataSource {

    suspend fun fetch(uid: String): Account
    suspend fun update(account: Account?)

}


class AccountDataSourceImpl(
    private val service: HttpService,
): AccountDataSource{

    override suspend fun fetch(uid: String): Account {
        return service.fetchAccount(uid)
    }

    override suspend fun update(account: Account?) {

    }
}
