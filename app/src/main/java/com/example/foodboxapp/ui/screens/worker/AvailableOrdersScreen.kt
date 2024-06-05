package com.example.foodboxapp.ui.screens.worker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.ui.composables.AddressComposable
import com.example.foodboxapp.ui.composables.CheckoutBar
import com.example.foodboxapp.ui.composables.OrderItemWithBottomSheet
import com.example.foodboxapp.ui.composables.OrderSummary
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
                OrderItemWithBottomSheet(order = it){
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

@Composable
fun OrderDetailsSheet(
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
