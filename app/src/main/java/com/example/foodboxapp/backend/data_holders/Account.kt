package com.example.foodboxapp.backend.data_holders

import com.example.foodboxapp.R
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String,
    val email: String,
    val address: Address = Address(),
    val type: AccountType = AccountType.Client,
    val paymentMethod: PaymentMethod? = null,
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

@Serializable
sealed class PaymentMethod{
    open val image: Int = R.drawable.mastercard_logo
    open val title: Int = R.string.credit_card
    companion object{
        fun methods() = listOf(Card, GooglePay, Paypal)


    }

    @Serializable
    data object Card: PaymentMethod(){
        override val image = R.drawable.mastercard_logo
        override val title = R.string.credit_card

    }
    @Serializable
    data object GooglePay: PaymentMethod(){
        override val image = R.drawable.google_pay_logo
        override val title = R.string.google_pay

    }
    @Serializable
    data object Paypal: PaymentMethod(){
        override val image = R.drawable.paypal_logo
        override val title = R.string.paypal

    }
}

fun String.toPaymentMethod(): PaymentMethod? {
    return when (this) {
        "Card" -> PaymentMethod.Card
        "GooglePay" -> PaymentMethod.GooglePay
        "Paypal" -> PaymentMethod.Paypal
        else -> null
    }
}

fun String.toAccountType(): AccountType {
    return when (this) {
        "Client" -> AccountType.Client
        "Worker" -> AccountType.Worker
        "Admin" -> AccountType.Admin
        // Map other strings to their corresponding PaymentMethod subclasses
        else -> throw IllegalArgumentException("Invalid value of AccountType")
    }
}