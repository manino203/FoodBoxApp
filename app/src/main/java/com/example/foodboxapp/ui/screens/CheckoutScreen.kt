package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Address
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.backend.data_holders.PaymentMethod
import com.example.foodboxapp.form.AddressForm
import com.example.foodboxapp.ui.composables.Category
import com.example.foodboxapp.ui.composables.CenteredLoading
import com.example.foodboxapp.ui.composables.CheckoutBar
import com.example.foodboxapp.ui.composables.FormComposable
import com.example.foodboxapp.ui.composables.OrderSummary
import com.example.foodboxapp.ui.composables.PaymentMethodSelector
import com.example.foodboxapp.ui.composables.rememberFormState
import com.example.foodboxapp.ui.composables.updateToolbarLoading
import com.example.foodboxapp.ui.composables.updateToolbarTitle
import com.example.foodboxapp.viewmodels.CheckoutUiState
import com.example.foodboxapp.viewmodels.CheckoutViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun CheckoutScreen(
    toolbarViewModel: ToolbarViewModel,
    actionOrderSent: () -> Unit
) {
    val viewModel: CheckoutViewModel = koinViewModel()
    val title = stringResource(id = R.string.checkout)

    updateToolbarTitle(toolbarViewModel, title)
    updateToolbarLoading(toolbarViewModel, viewModel.uiState.value.loading)

    LaunchedEffect(Unit) {
        viewModel.loadCartItems()
        viewModel.loadAccount()
    }
    val account by remember(viewModel.uiState.value.account) {
        mutableStateOf(viewModel.uiState.value.account)
    }


    account?.let{
        CheckoutScreen(
            viewModel.uiState.value,
            {
                viewModel.updatePaymentMethod(it)
            }
        ) {
            viewModel.sendOrder(it) {
                actionOrderSent()
            }
        }
    } ?: CenteredLoading()

}

@Composable
private fun CheckoutScreen(
    uiState: CheckoutUiState,
    actionChangePaymentMethod: (PaymentMethod) -> Unit,
    actionPay: (Order) -> Unit
){
    var address by remember {
        mutableStateOf( uiState.account?.address?.let{
            Address(
                it.street,
                it.city,
                it.zipCode,
                it.country
            )
        }?: Address())
    }
    val addressFormState = rememberFormState(
        form = AddressForm(
            address.street,
            address.city,
            address.zipCode,
            address.country
        ){
            address = Address(
                it.street,
                it.city,
                it.zipCode,
                it.country
            )
        }
    )
    val summaryCategories by remember(uiState.cartItems) {
        mutableStateOf(uiState.cartItems.map{ it.store }.toSet().map{ store ->
            Pair(store.title, uiState.cartItems.filter { store.title == it.store.title })
        })
    }
    Scaffold(bottomBar = {
        CheckoutBar(total = uiState.total, buttonTitle = stringResource(id = R.string.pay)) {
            actionPay(
                Order(
                    items = uiState.cartItems,
                    "",
                    address,
                    uiState.cartItems.map { it.store }.toSet().toList(),
                    uiState.total
                )
            )
        }
    }){ paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            Category(title = R.string.delivery_address) {
                FormComposable(addressFormState)
            }
            Category(title = R.string.payment_method) {
                PaymentMethodSelector(selected = {
                    it == uiState.paymentMethod
                }) {
                    actionChangePaymentMethod(it)
                }
            }

            OrderSummary(summaryCategories, showDivider = false)

        }
    }
}


