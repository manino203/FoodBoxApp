package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.Address
import com.example.foodboxapp.backend.data_sources.AccountDataSource

interface AccountRepository {
    val account: Account
}

class AccountRepositoryImpl(
    private val dataSource: AccountDataSource
): AccountRepository{
    override val account = Account(
        "email@test.com",
            Address(
                "17.novembra 1296",
                "Cadca",
                "02204",
                "Slovakia"
            )
        )
}