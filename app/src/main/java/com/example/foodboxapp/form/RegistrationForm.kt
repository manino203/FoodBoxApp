package com.example.foodboxapp.form

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.foodboxapp.R

data class FilledRegistrationForm(
    val username: String,
    val email: String,
    val password: String
)

class RegistrationForm(username: String,
                       email: String,
                       password: String,
                       actionSubmit: (FilledRegistrationForm) -> Unit
): Form(
    fields = listOf(
        Field(
            label = R.string.username,
            placeholder = null,
            validators = listOf(RequiredValidator),
            value = username,
            help = null,
            type = FieldType.Text,
            isRequired = true
        ),
        Field(
            label = R.string.email,
            placeholder = null,
            validators = listOf(RequiredValidator, EmailValidator),
            value = username,
            help = null,
            type = FieldType.Text,
            isRequired = true
        ),
        Field(
            label = R.string.password,
            placeholder = null,
            validators = listOf(RequiredValidator),
            value = password,
            help = null,
            type = FieldType.Password,
            isRequired = true
        )
    ),
    captureInitialFocus = false,
    submitLabel = R.string.register,
    onSubmit = {
        actionSubmit(
            FilledRegistrationForm(
                username = it[0],
                email = it[1],
                password = it[2]
            )
        )
    })