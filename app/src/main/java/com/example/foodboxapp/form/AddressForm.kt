package com.example.foodboxapp.form

import com.example.foodboxapp.R

data class FilledAddressForm(
    val street: String,
    val city: String,
    val zipCode: String,
    val country: String
)

class AddressForm(
    street: String,
    city: String,
    zipCode: String,
    country: String,
    actionSubmit: (FilledAddressForm) -> Unit
): Form(
    fields = listOf(
        Field(
            label = R.string.address_street,
            placeholder = null,
            validators = listOf(RequiredValidator),
            value = street,
            help = null,
            type = FieldType.Text,
            isRequired = true
        ),
        Field(
            label = R.string.address_city,
            placeholder = null,
            validators = listOf(RequiredValidator),
            value = city,
            help = null,
            type = FieldType.Text,
            isRequired = true
        ),
        Field(
            label = R.string.address_zip_code,
            placeholder = null,
            validators = listOf(RequiredValidator, IntegerValidator),
            value = zipCode,
            help = null,
            type = FieldType.Text,
            isRequired = true
        ),
        Field(
            label = R.string.address_country,
            placeholder = null,
            validators = listOf(RequiredValidator),
            value = country,
            help = null,
            type = FieldType.Text,
            isRequired = true
        )
    ),
    captureInitialFocus = false,
    submitLabel = R.string.confirm,
    onSubmit = {
        actionSubmit(
            FilledAddressForm(
                street = it[0],
                city = it[1],
                zipCode = it[2],
                country = it[3]
            )
        )
    }
)