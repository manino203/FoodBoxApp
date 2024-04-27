package com.example.foodboxapp.backend

import com.example.foodboxapp.R

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