package com.example.foodboxapp.form

class FormFieldValidator(
    val error: FormError,
    private val validator: (String) -> Boolean
) {
    fun validate(value: (String)): Boolean = validator(value)
}

val MyheatpumpAccountValidator = FormFieldValidator(FormError.InvalidFormat) {
    it.matches(Regex("^[a-zA-Z0-9_-]{1,64}$"))
}

val RequiredValidator = FormFieldValidator(FormError.Required) {
    it.isNotEmpty()
}

val IntegerValidator = FormFieldValidator(FormError.NotInteger) {
    it.matches(Regex("^[0-9]+$"))
}

val EmailValidator = FormFieldValidator(FormError.InvalidFormat) {
    // android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()

    it.matches(
        Regex(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
    )

}

val PhoneValidator = FormFieldValidator(FormError.InvalidFormat) {
    // android.util.Patterns.PHONE.matcher(it).matches()

    it.matches(
        Regex(
            "(\\+[0-9]+[\\- .]*)?" + // +<digits><sdd>*
                    "(\\([0-9]+\\)[\\- .]*)?" + // (<digits>)<sdd>*
                    "([0-9][0-9\\- .]+[0-9])" // <digit><digit|sdd>+<digit>
        )
    )
}

// Used when add/editing a user
val MyHeatPumpDateValidator = FormFieldValidator(FormError.InvalidFormat) {
    it.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))
}

// User when creating/editing a device
val MyheatpumpDateTimeValidator = FormFieldValidator(FormError.InvalidFormat) {
    it.matches(Regex("^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}$"))
}
