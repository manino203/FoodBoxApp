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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodboxapp.R
import com.example.foodboxapp.form.FilledRegistrationForm
import com.example.foodboxapp.form.LoginForm
import com.example.foodboxapp.form.RegistrationForm
import com.example.foodboxapp.ui.composables.AutoErrorBox
import com.example.foodboxapp.ui.composables.FormComposable
import com.example.foodboxapp.ui.composables.rememberFormState
import com.example.foodboxapp.viewmodels.LoginViewModel
import com.example.foodboxapp.viewmodels.RegistrationUiState
import com.example.foodboxapp.viewmodels.RegistrationViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel

@Composable
fun RegistrationScreen(
    toolbarViewModel: ToolbarViewModel,
    alreadyHaveAccountAction: () -> Unit
) {
    val viewModel: RegistrationViewModel = viewModel()
    val title = stringResource(id = R.string.register)

    LaunchedEffect(title) {
        toolbarViewModel.updateTitle(title)
    }
    LaunchedEffect(Unit) {
        toolbarViewModel.updateLoading(false)
    }
    RegistrationScreen(
        uiState = viewModel.uiState.value,
        { form->
            viewModel.register(form)
        }
    ){
        alreadyHaveAccountAction()
    }
}
@Composable
private fun RegistrationScreen(
    uiState: RegistrationUiState,
    actionRegister: (FilledRegistrationForm) -> Unit,
    actionBackToLogin: () -> Unit
) {
    Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(20.dp),
    modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(20.dp)
        .fillMaxHeight()
    ) {
    //        Image(painter = painterResource(id = R.drawable.ic_logo_attack), "Attack logo")

        AutoErrorBox(uiState.error)

        val registrationFormState = rememberFormState(
            RegistrationForm(
                username = "",
                email = "",
                password = "",
                actionSubmit = {
                    actionRegister(it)
                },
            )
        )
        Box(Modifier.animateContentSize()) {
            Crossfade(targetState = uiState.loading, label = "") {
                if (it) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .wrapContentSize()
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    FormComposable(registrationFormState)
                }
            }
        }

        Text(
            stringResource(R.string.already_have_account),
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                actionBackToLogin()
            })
}
}

@Preview
@Composable
private fun RegistrationScreenPreview(){
    RegistrationScreen(
        RegistrationUiState(),
        {_ ->}
    ) {

    }
}
