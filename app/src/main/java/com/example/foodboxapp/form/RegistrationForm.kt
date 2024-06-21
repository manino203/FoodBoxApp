package com.example.foodboxapp.form

import com.example.foodboxapp.R

data class FilledRegistrationForm(
    val username: String,
    val email: String,
    val password: String
)

class RegistrationForm(
                       email: String,
                       password: String,
                       confirmPassword: String,
                       actionSubmit: (FilledRegistrationForm) -> Unit
): Form(
    fields = listOf(
        Field(
            label = R.string.email,
            placeholder = null,
            validators = listOf(RequiredValidator, EmailValidator),
            value = email,
            help = null,
            type = FieldType.Text,
            isRequired = true
        ),
        Field(
            label = R.string.password,
            placeholder = null,
            validators = listOf(RequiredValidator, PasswordValidator),
            value = password,
            help = null,
            type = FieldType.Password,
            isRequired = true
        ),

        Field(
            label = R.string.confirm_password,
            placeholder = null,
            validators = listOf(RequiredValidator, PasswordValidator),
            value = confirmPassword,
            help = null,
            type = FieldType.ConfirmPassword,
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