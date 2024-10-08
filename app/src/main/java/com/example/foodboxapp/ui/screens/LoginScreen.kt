package com.example.foodboxapp.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.form.LoginForm
import com.example.foodboxapp.ui.composables.AutoErrorBox
import com.example.foodboxapp.ui.composables.FormComposable
import com.example.foodboxapp.ui.composables.rememberFormState
import com.example.foodboxapp.ui.composables.updateToolbarLoading
import com.example.foodboxapp.ui.composables.updateToolbarTitle
import com.example.foodboxapp.viewmodels.LoginUiState
import com.example.foodboxapp.viewmodels.LoginViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    toolbarViewModel: ToolbarViewModel,
    defaultUsername: String = "",
    actionRegister: () -> Unit
) {
    val viewModel: LoginViewModel = koinViewModel()
    val title = stringResource(id = R.string.login)

    updateToolbarTitle(toolbarViewModel, title)
    updateToolbarLoading(toolbarViewModel, viewModel.uiState.value.isLoggingIn)
    LoginScreen(
        uiState = viewModel.uiState.value,
        defaultEmail = defaultUsername,
        { username, password ->
            viewModel.login(username, password)
        }
    ){
        actionRegister()
    }
}

@Composable
private fun LoginScreen(
    uiState: LoginUiState,
    defaultEmail: String?,
    actionLogin: (String, String) -> Unit = { _, _ -> },
    actionRegister: () -> Unit = {}
){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .fillMaxHeight()
    ) {

        AutoErrorBox(uiState.error)

        val loginFormState = rememberFormState(
            LoginForm(
                email = defaultEmail.orEmpty(),
                password = "",
                actionSubmit = {
                    actionLogin(it.email, it.password)
                },
            )
        )
        Box(Modifier.animateContentSize()) {
            Crossfade(targetState = uiState.isLoggingIn, label = "") {
                if (it) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .wrapContentSize()) {
                        CircularProgressIndicator()
                    }
                } else {
                    FormComposable(loginFormState)
                }
            }
        }

        Text(
            stringResource(R.string.register),
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                actionRegister()
            })

//        Divider()

//        Text(
//            Strings.login_as_demo.translate(context),
//            color = MaterialTheme.colorScheme.primary,
//            textDecoration = TextDecoration.Underline,
//            modifier = Modifier.clickable {
//                loginFormState.values[0].value = DEMO_ACCOUNT_USERNAME
//                loginFormState.values[1].value = DEMO_ACCOUNT_PASSWORD
//
//                actionLogin(DEMO_ACCOUNT_USERNAME, DEMO_ACCOUNT_PASSWORD)
//            })
    }
}