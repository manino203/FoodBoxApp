package com.example.foodboxapp.backend.repositories

import com.example.foodboxapp.backend.data_sources.AccountDataSource

interface AccountRepository {
}

class AccountRepositoryImpl(
    private val dataSource: AccountDataSource
): AccountRepository{

}