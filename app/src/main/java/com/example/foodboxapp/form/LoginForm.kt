package com.example.foodboxapp.form

import com.example.foodboxapp.R

data class FilledLoginForm(
    val username: String,
    val password: String
)

class LoginForm(
    username: String,
    password: String,
    actionSubmit: (FilledLoginForm) -> Unit
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
    submitLabel = R.string.login,
    onSubmit = {
        actionSubmit(
            FilledLoginForm(
                username = it[0],
                password = it[1]
            )
        )
    }
)