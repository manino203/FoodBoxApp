package com.example.foodboxapp.backend.data_sources

import com.example.foodboxapp.R


interface AccountDataSource {
}

class AccountDataSourceImpl(): AccountDataSource{

}

sealed class AccountType{
    abstract val title:  Int

    data object Client: AccountType(){
        override val title: Int = R.string.client
    }
    data object Employee: AccountType(){
        override val title: Int = R.string.employee
    }

    data object Admin: AccountType(){
        override val title: Int = R.string.admin
    }
}