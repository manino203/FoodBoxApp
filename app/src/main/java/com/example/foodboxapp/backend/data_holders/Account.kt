package com.example.foodboxapp.backend.data_holders

import com.example.foodboxapp.R
import com.example.foodboxapp.viewmodels.PaymentMethod

data class Account(
    val email: String,
    val address: Address,
    val type: AccountType = AccountType.Client,
    val paymentMethod: PaymentMethod = PaymentMethod.Card
)

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