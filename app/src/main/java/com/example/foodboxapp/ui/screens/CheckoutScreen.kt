package com.example.foodboxapp.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Address
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.form.AddressForm
import com.example.foodboxapp.ui.composables.CartItem
import com.example.foodboxapp.ui.composables.CheckoutBar
import com.example.foodboxapp.ui.composables.FormComposable
import com.example.foodboxapp.ui.composables.rememberFormState
import com.example.foodboxapp.viewmodels.CheckoutUiState
import com.example.foodboxapp.viewmodels.CheckoutViewModel
import com.example.foodboxapp.viewmodels.PaymentMethod
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun CheckoutScreen(
    toolbarViewModel: ToolbarViewModel,
    actionOrderSent: () -> Unit
) {
    val viewModel: CheckoutViewModel = koinViewModel()
    val title = stringResource(id = R.string.checkout)
    LaunchedEffect(title) {
        toolbarViewModel.updateTitle(title)
    }

    LaunchedEffect(viewModel.uiState.value.loading) {
        toolbarViewModel.updateLoading(viewModel.uiState.value.loading)
    }

    LaunchedEffect(Unit) {
        viewModel.loadCartItems()
    }



    CheckoutScreen(
        viewModel.uiState.value,
        {
            viewModel.updatePaymentMethod(it)
        }
    ){
        viewModel.sendOrder(it){
            actionOrderSent()
        }
    }

}

@Composable
private fun CheckoutScreen(
    uiState: CheckoutUiState,
    actionChangePaymentMethod: (PaymentMethod) -> Unit,
    actionPay: (Order) -> Unit
){
    var address by remember {
        mutableStateOf(Address())
    }
    val addressFormState = rememberFormState(
        form = AddressForm(
            uiState.account.address.street,
            uiState.account.address.city,
            uiState.account.address.zipCode,
            uiState.account.address.country
        ){
            address = Address(
                it.street,
                it.city,
                it.zipCode,
                it.country
            )
        }
    )
    Scaffold(bottomBar = {
        CheckoutBar(total = uiState.total, buttonTitle = stringResource(id = R.string.pay)) {
            actionPay(
                Order(
                    items = uiState.cartItems,
                    uiState.account,
                    address,
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

            Category(R.string.summary, false) {
                uiState.cartItems.forEach {item ->
                    CartItem(item = item, modifier = Modifier.padding(16.dp, 8.dp), editable = false)
                }
            }
        }
    }
}



@Suppress("FunctionName")
private fun LazyListScope.Category(
    @StringRes title: Int,
    showDivider: Boolean = true,
    content: @Composable () -> Unit
){
    item{
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(title),
            color = MaterialTheme.colorScheme.primary
        )
    }
    item{
        content()
    }
    if (showDivider){
        item {
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
        }
    }
}

@Composable
private fun PaymentMethodSelector(
    selected: (PaymentMethod) -> Boolean,
    actionChangePaymentMethod: (PaymentMethod) -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PaymentMethod.methods().forEach {
            Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { actionChangePaymentMethod(it) }
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        stringResource(id = it.title).also { title ->
                            Image(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(id = it.image),
                                contentDescription = title
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = title
                            )
                        }
                    }
                    RadioButton(
                        selected = selected(it),
                        onClick = { actionChangePaymentMethod(it) })
                }
            }
        }
    }
}
