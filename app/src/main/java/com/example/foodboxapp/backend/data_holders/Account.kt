package com.example.foodboxapp.backend.data_holders

import com.example.foodboxapp.R
import com.example.foodboxapp.viewmodels.PaymentMethod
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val email: String,
    val address: Address,
    val type: AccountType = AccountType.Worker,
    val paymentMethod: PaymentMethod = PaymentMethod.GooglePay
)

@Serializable
sealed class AccountType{
    abstract val title:  Int

    data object Client: AccountType(){
        override val title: Int = R.string.client
    }
    data object Worker: AccountType(){
        override val title: Int = R.string.employee
    }

    data object Admin: AccountType(){
        override val title: Int = R.string.admin
    }
}