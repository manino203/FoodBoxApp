package com.example.foodboxapp.ui.screens.worker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.ui.composables.BottomSheet
import com.example.foodboxapp.ui.composables.CheckoutBar
import com.example.foodboxapp.ui.composables.OrderSummary
import com.example.foodboxapp.ui.composables.Price
import com.example.foodboxapp.ui.composables.open
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import com.example.foodboxapp.viewmodels.worker.AvailableOrdersUiState
import com.example.foodboxapp.viewmodels.worker.AvailableOrdersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AvailableOrdersScreen(
    toolbarViewModel: ToolbarViewModel
) {
    val viewModel: AvailableOrdersViewModel = koinViewModel()
    val title = stringResource(id = R.string.available_orders)
    LaunchedEffect(title) {
        toolbarViewModel.updateTitle(title)
    }

    LaunchedEffect(viewModel.uiState.value.loading) {
        toolbarViewModel.updateLoading(viewModel.uiState.value.loading)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchOrders()
    }

    AvailableOrdersScreen(uiState = viewModel.uiState.value) {
        viewModel.acceptOrder(it)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AvailableOrdersScreen(
    uiState: AvailableOrdersUiState,
    actionAcceptOrder: (Order) -> Unit
){
    if(uiState.orders.isNotEmpty()){
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(uiState.orders) {
                AvailableOrder(order = it){
                    actionAcceptOrder(it)
                }
            }
        }
    }else{
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(text = stringResource(id = R.string.no_orders))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AvailableOrder(
    order: Order,
    actionAcceptOrder: () -> Unit
){
    val sheetOpen = remember{
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val coroScope = rememberCoroutineScope()
    BottomSheet(showSheet = sheetOpen, sheetState = sheetState, coroScope = coroScope) {
        OrderDetailsSheet(order = order, dismiss = it) {
            actionAcceptOrder()
            it()
        }
    }


    Card(
        { sheetState.open(sheetOpen, coroScope) },
        Modifier.fillMaxWidth()
    ){
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(text = "${stringResource(id = R.string.order_id)}: ${order.id}")
            HorizontalDivider()

            AddressComposable(
                street = order.address.street,
                city = order.address.city,
                zipCode = order.address.zipCode,
                country = order.address.country
            )
            Text(
                text = "${stringResource(id = R.string.stores)}: ${
                    buildString {
                        order.stores.forEachIndexed { index, it ->
                            append(if (index != order.stores.lastIndex) "${it.title}, " else it.title)
                        }
                    }
                }"
            )
            Row {
                Text(
                    text = "${stringResource(id = R.string.cart_total)}: ",
                    fontWeight = FontWeight.Bold
                )
                Price(
                    price = order.total,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun OrderDetailsSheet(
    order: Order,
    dismiss: () -> Unit,
    actionAccept: () -> Unit
){

    val storeCategories by remember(order.items) {
        mutableStateOf(
            order.stores.map{ store ->
                Pair(store.title, order.items.filter { store.title == it.store.title })
            }
        )
    }

    Scaffold(
        bottomBar = {
            CheckoutBar(total = order.total, buttonTitle = stringResource(R.string.accept_order)) {
                actionAccept()
            }
        },
        topBar = {
            AddressComposable(
                street = order.address.street,
                city = order.address.city,
                zipCode = order.address.zipCode,
                country = order.address.country
            )
        }
    ){ padding ->
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OrderSummary(
                storeCategories
            )
        }
    }
}

@Composable
private fun AddressComposable(
    street: String,
    city: String,
    zipCode: String,
    country: String
){
    Column{
        Text(
            text = stringResource(id = R.string.delivery_address),
            color = MaterialTheme.colorScheme.primary
        )
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = street)
            Text(text = city)
            Text(text = zipCode)
            Text(text = country)
        }
        HorizontalDivider()
    }
}
