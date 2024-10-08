package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Address
import com.example.foodboxapp.backend.data_holders.PaymentMethod
import com.example.foodboxapp.form.AddressForm
import com.example.foodboxapp.ui.composables.AutoErrorBox
import com.example.foodboxapp.ui.composables.Category
import com.example.foodboxapp.ui.composables.CenteredLoading
import com.example.foodboxapp.ui.composables.FormComposable
import com.example.foodboxapp.ui.composables.PaymentMethodSelector
import com.example.foodboxapp.ui.composables.ShowErrorToast
import com.example.foodboxapp.ui.composables.rememberFormState
import com.example.foodboxapp.ui.composables.updateToolbarLoading
import com.example.foodboxapp.ui.composables.updateToolbarTitle
import com.example.foodboxapp.viewmodels.AccountSettingsUiState
import com.example.foodboxapp.viewmodels.AccountSettingsViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountSettingsScreen(
    toolbarViewModel: ToolbarViewModel
) {
    val viewModel: AccountSettingsViewModel = koinViewModel()
    val title = stringResource(id = R.string.account_settings)

    updateToolbarTitle(toolbarViewModel, title)
    updateToolbarLoading(toolbarViewModel, viewModel.uiState.value.loading)

    LaunchedEffect(Unit) {
        viewModel.collectAccount()

    }
    val account by remember(viewModel.uiState.value.account) {
        mutableStateOf(viewModel.uiState.value.account)
    }


    account?.let{ acc ->
        AccountSettingsScreen(
            uiState = viewModel.uiState.value,
            actionModifyAddress = {
                viewModel.modify(acc.id, acc.email, it, acc.paymentMethod)

            }
        ) {
            viewModel.modify(acc.id, acc.email, acc.address, it)
        }
    } ?: CenteredLoading()
}

@Composable
private fun AccountSettingsScreen(
    uiState: AccountSettingsUiState,
    actionModifyAddress: (Address) -> Unit,
    actionModifyPaymentMethod: (PaymentMethod) -> Unit
){
    val addressFormState = rememberFormState(
        form = AddressForm(
            uiState.account!!.address.street,
            uiState.account.address.city,
            uiState.account.address.zipCode,
            uiState.account.address.country
        ){
            actionModifyAddress(Address(
                it.street,
                it.city,
                it.zipCode,
                it.country
            ))
        }
    )

    ShowErrorToast(error = uiState.error)


    LazyColumn(Modifier.fillMaxSize()) {
        item{
            AutoErrorBox(uiState.error)
        }
        Category(title = R.string.delivery_address) {
            FormComposable(addressFormState)
        }
        Category(title = R.string.payment_method) {
            PaymentMethodSelector(selected = {
                it == uiState.account.paymentMethod
            }) {
                actionModifyPaymentMethod(it)
            }
        }
    }
}