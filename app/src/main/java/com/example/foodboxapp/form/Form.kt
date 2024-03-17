package com.example.foodboxapp.form

import androidx.annotation.StringRes
import com.example.foodboxapp.R

enum class FieldType {
    Hidden,
    Text,
    TextArea,
    Password,
    Date,
    Scanner,
    Checkbox
}

enum class FieldKeyboardType {
    Text,
    Email,
    Number
}

data class Field(
    @StringRes val label: Int,
    val value: String? = null,
    val placeholder: String? = null,
    val help: String? = null,
    val type: FieldType = FieldType.Text,
    val validators: List<FormFieldValidator> = listOf(),
    val isRequired: Boolean = false,
    val isEnabled: Boolean = true,
    val keyboardType: FieldKeyboardType = FieldKeyboardType.Text,
    val autoCapitalize: Boolean = false,
    val autoCorrect: Boolean = false
)

open class Form(
    val fields: List<Field>,
    val captureInitialFocus: Boolean = true,
    @StringRes val submitLabel: Int,
    val onSubmit: (List<String>) -> Unit
)

interface FormValidationResult

enum class FormError {
    Required,
    InvalidFormat,
    NotInteger,
}

@StringRes
fun FormError.toResourceString(): Int {
    return when (this) {
        FormError.Required -> R.string.field_error_required
        FormError.InvalidFormat -> R.string.field_error_invalid_format
        FormError.NotInteger -> R.string.field_error_must_be_integer
    }
}