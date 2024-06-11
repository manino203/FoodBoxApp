package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.foodboxapp.ui.ui.theme.FoodBoxThemeWithSurface
import com.example.foodboxapp.R
import com.example.foodboxapp.form.FieldKeyboardType
import com.example.foodboxapp.form.FieldType
import com.example.foodboxapp.form.Form
import com.example.foodboxapp.form.LoginForm
import com.example.foodboxapp.form.toResourceString
import kotlinx.coroutines.launch

data class FormState(
    val form: Form,
    val values: List<MutableState<String>>,
    val errors: List<MutableState<String?>>,
    val focusRequesters: List<FocusRequester>,
    val submitFocus: FocusRequester
)

@Composable
fun rememberFormState(form: Form): FormState {
    val values = form.fields.map { rememberSaveable(it) { mutableStateOf(it.value.orEmpty()) } }
    val errors = form.fields.map { rememberSaveable(it) { mutableStateOf<String?>(null) } }
    val focusRequesters = form.fields.map { remember { FocusRequester() } }
    val submitFocus = remember { FocusRequester() }

    return FormState(
        form = form,
        values = values,
        errors = errors,
        focusRequesters = focusRequesters,
        submitFocus = submitFocus
    )
}

@Composable
fun FormComposable(form: Form) {
    FormComposable(rememberFormState(form))
}

@Composable
fun FormComposable(state: FormState, modifier: Modifier = Modifier) {
    val form = state.form
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusRequesters = state.focusRequesters
    val submitFocus = state.submitFocus
    val values = state.values
    val errors = state.errors
    val focusManager = LocalFocusManager.current

    val actionSubmit = {
        var isFormValid = true

        form.fields.forEachIndexed { i, f ->
            values[i].value = values[i].value.trim()

            val error = f.validators
                .firstOrNull { !it.validate(values[i].value) }?.error

            if (isFormValid && error != null) {
                isFormValid = false
                focusRequesters[i].requestFocus()
            }

            errors[i].value = error?.toResourceString()?.let { context.getString(it)}
        }

        if (isFormValid) {
            // This should clear software keyboard
            focusManager.clearFocus()

            form.onSubmit(values.map { it.value }.toList())
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        form.fields.forEachIndexed { index, field ->
            val isLast = index == form.fields.count() - 1
            var value by values[index]
            val focus = focusRequesters[index]
            var wasFocused by remember { mutableStateOf(false) }
            var error by errors[index]
            val supportingText: @Composable () -> Unit = remember(error) {
                {
                    val err = error
                    if (err != null) {
                        Text(err, color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            val actionFocusNext = {
                focus.freeFocus()
                if (index < form.fields.count() - 1) {
                    focusRequesters[index + 1].requestFocus()
                } else {
                    submitFocus.requestFocus()
                }
            }
            val actionFocusPrevious = {
                focus.freeFocus()
                if (index > 0) {
                    focusRequesters[index - 1].requestFocus()
                }
            }

            val imeAction = if (field.type == FieldType.TextArea) {
                ImeAction.None
            } else {
                when (isLast) {
                    true -> ImeAction.Done
                    false -> ImeAction.Next
                }
            }
            val capitalization = when (field.autoCapitalize) {
                true -> KeyboardCapitalization.Sentences
                false -> KeyboardCapitalization.None
            }
            val autoCorrect = field.autoCorrect
            val keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                capitalization = capitalization,
                autoCorrect = autoCorrect,
                keyboardType = when (field.keyboardType) {
                    FieldKeyboardType.Text -> KeyboardType.Text
                    FieldKeyboardType.Email -> KeyboardType.Email
                    FieldKeyboardType.Number -> KeyboardType.Number
                }
            )
            val keyboardActions = KeyboardActions(
                onPrevious = { actionFocusPrevious() },
                onNext = { actionFocusNext() },
                onDone = { actionSubmit() }
            )

            @OptIn(ExperimentalComposeUiApi::class)
            val fieldModifier = Modifier
                .focusRequester(focus)
                .onFocusChanged { focusState ->
                    if (wasFocused) {
                        if (field.type != FieldType.Password) {
                            value = value.trim()
                        }

                        scope.launch {
                            val validationError = field.validators.firstOrNull { v ->
                                !v.validate(value)
                            }?.error

                            error = validationError
                                ?.toResourceString()
                                ?.let { context.getString(it) }
                        }
                    }

                    wasFocused = focusState.isFocused
                }
                .onPreviewKeyEvent { event: KeyEvent ->
                    when (event.type) {
                        KeyEventType.KeyUp -> {
                            when (event.key) {
                                Key.Enter -> if (field.type != FieldType.TextArea) {
                                    actionSubmit()
                                }

                                Key.NumPadEnter -> if (field.type != FieldType.TextArea) {
                                    actionSubmit()
                                }

                                else -> return@onPreviewKeyEvent false
                            }

                            return@onPreviewKeyEvent true
                        }

                        KeyEventType.KeyDown -> {
                            when (event.key) {
                                Key.Tab -> {
                                    if (event.isShiftPressed) {
                                        actionFocusPrevious()
                                    } else {
                                        actionFocusNext()
                                    }
                                }

                                else -> return@onPreviewKeyEvent false
                            }

                            return@onPreviewKeyEvent true
                        }
                    }

                    return@onPreviewKeyEvent false
                }

            when (field.type) {
                FieldType.Hidden -> {}
                FieldType.Text ->
                    TextField(
                        label = { Text(context.getString(field.label)) },
                        value = value,
                        placeholder = { field.placeholder },
                        enabled = field.isEnabled,
                        onValueChange = { value = it },
                        modifier = fieldModifier,
                        isError = error != null,
                        singleLine = true,
                        supportingText = supportingText,
                        keyboardActions = keyboardActions,
                        keyboardOptions = keyboardOptions,
                    )
                FieldType.Password -> {
                    var passwordVisible by remember { mutableStateOf(false) }
                    TextField(
                        label = { Text(context.getString(field.label)) },
                        value = value,
                        placeholder = { field.placeholder },
                        enabled = field.isEnabled,
                        onValueChange = { value = it },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        trailingIcon = {


                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(painterResource(
                                    id = if (passwordVisible)
                                        R.drawable.visibility
                                    else
                                        R.drawable.visibility_off),
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = fieldModifier,
                        isError = error != null,
                        singleLine = true,
                        supportingText = supportingText,
                        keyboardActions = keyboardActions,
                        keyboardOptions = keyboardOptions
                    )
                }

//                FieldType.Date -> @OptIn(ExperimentalMaterial3Api::class) {
//                    var isDialogOpen by remember { mutableStateOf(false) }
//                    val currentMonth = remember {
//                        UtcTimestamp(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())).milliseconds
//                    }
//                    val timestamp = remember(value) { UtcTimestamp.fromIsoString(value) }
//                    val dpState = rememberDatePickerState(
//                        timestamp?.milliseconds,
//                        timestamp?.milliseconds ?: currentMonth
//                    )
//                    val formattedValue = remember(timestamp) {
//                        timestamp?.let {
//                            DateFormat
//                                .getDateInstance(DateFormat.LONG, Locale.getDefault())
//                                .format(Date(it.toLocalDateTime().toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()))
//                        }.orEmpty()
//                    }
//                    if (isDialogOpen) {
//                        CalendarDialog(
//                            state = dpState,
//                            onDismiss = { isDialogOpen = false },
//                            onConfirm = {
//                                value = dpState.selectedDateMillis?.let { UtcTimestamp(it).toIsoString() }.orEmpty()
//                                isDialogOpen = false
//                            }
//                        )
//                    }
//                    TextField(
//                        label = { Text(context.getString(field.label)) },
//                        value = formattedValue,
//                        placeholder = { field.placeholder },
//                        enabled = field.isEnabled,
//                        onValueChange = {},
//                        modifier = fieldModifier,
//                        isError = error != null,
//                        singleLine = true,
//                        readOnly = true,
//                        supportingText = supportingText,
//                        keyboardActions = keyboardActions,
//                        keyboardOptions = keyboardOptions,
//                        trailingIcon = {
//                            IconButton(onClick = { isDialogOpen = true }) {
//                                Icon(Icons.Default.CalendarMonth, contentDescription = null)
//                            }
//                        },
//                    )
//                }

//                FieldType.Scanner -> {
//                    val scannerLauncher = rememberLauncherForActivityResult(
//                        ActivityResultContracts.StartActivityForResult()
//                    ) {
//                        if (it.resultCode == AppCompatActivity.RESULT_OK) {
//                            it.data?.getStringExtra("SCAN_RESULT")?.let { result ->
//                                value = result
//                            }
//                        }
//                    }
//                    TextField(
//                        label = { Text(context.getString(field.label)) },
//                        value = value,
//                        placeholder = { field.placeholder },
//                        enabled = field.isEnabled,
//                        onValueChange = { value = it },
//                        modifier = fieldModifier,
//                        isError = error != null,
//                        singleLine = true,
//                        supportingText = supportingText,
//                        keyboardActions = keyboardActions,
//                        keyboardOptions = keyboardOptions,
//                        trailingIcon = {
//                            IconButton(onClick = {
//                                scannerLauncher.launch(
//                                    ScanOptions().apply {
//                                        setBeepEnabled(false)
//                                        setDesiredBarcodeFormats(ScanOptions.CODE_39)
//                                        setOrientationLocked(false)
//                                        setPrompt(context.resources.getString(R.string.scan_product_no_prompt))
//                                    }.createScanIntent(context)
//                                )
//                            }) {
//                                Icon(Icons.Default.CameraAlt, contentDescription = null)
//                            }
//                        },
//                    )
//                }
                FieldType.TextArea -> {
                    TextField(
                        label = { Text(context.getString(field.label)) },
                        value = value,
                        placeholder = { field.placeholder },
                        enabled = field.isEnabled,
                        onValueChange = { value = it },
                        modifier = fieldModifier,
                        isError = error != null,
                        singleLine = false,
                        supportingText = supportingText,
                        keyboardActions = keyboardActions,
                        keyboardOptions = keyboardOptions
                    )
                }
                FieldType.Checkbox -> {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(context.getString(field.label))
                        Switch(
                            checked = value.toBoolean(),
                            onCheckedChange = { value = it.toString() },
                            enabled = field.isEnabled
                        )
                    }
                }

                FieldType.Date -> {

                }
                FieldType.Scanner -> {

                }
            }
        }

        Button(
            onClick = actionSubmit,
            modifier = Modifier.focusRequester(submitFocus)
        ) {
            Text(context.getString(form.submitLabel))
        }
    }

    if (form.captureInitialFocus) {
        LaunchedEffect(Unit) {
            focusRequesters.first().requestFocus()
        }
    }
}

@Composable
private fun FormComposablePreview() = FoodBoxThemeWithSurface {
    val form = LoginForm(
        email = "",
        password = "",
        actionSubmit = {}
    )

    Column {
        FormComposable(form = form)
    }
}
