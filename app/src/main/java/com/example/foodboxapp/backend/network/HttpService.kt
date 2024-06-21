package com.example.foodboxapp.backend.network

import com.example.foodboxapp.backend.data_holders.Account
import com.example.foodboxapp.backend.data_holders.sampleAddress

interface HttpService {
    suspend fun fetchAccount(uid: String): Account
}

class HttpServiceImpl(): HttpService{
    override suspend fun fetchAccount(uid: String): Account {
        return Account(
            "test@test.sk",
            sampleAddress,
        )
    }


}
