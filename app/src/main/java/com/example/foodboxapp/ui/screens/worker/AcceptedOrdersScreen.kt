package com.example.foodboxapp.ui.screens.worker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.ui.composables.OrderItem
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import com.example.foodboxapp.viewmodels.worker.AcceptedOrdersUiState
import com.example.foodboxapp.viewmodels.worker.AcceptedOrdersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AcceptedOrdersScreen(
    toolbarViewModel: ToolbarViewModel,
    actionNavToOrder: (Int) -> Unit
) {
    val viewModel: AcceptedOrdersViewModel = koinViewModel()
    val title = stringResource(id = R.string.accepted_orders)
    LaunchedEffect(title) {
        toolbarViewModel.updateTitle(title)
    }

    LaunchedEffect(viewModel.uiState.value.loading) {
        toolbarViewModel.updateLoading(viewModel.uiState.value.loading)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchOrders()
    }

    AcceptedOrdersScreen(uiState = viewModel.uiState.value){
        actionNavToOrder(it.id)
    }

}


@Composable
private fun AcceptedOrdersScreen(
    uiState: AcceptedOrdersUiState,
    actionClickOrder: (Order) -> Unit
){
    if(uiState.orders.isNotEmpty()){
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.orders) {
                OrderItem(order = it){
                    actionClickOrder(it)
                }
            }
        }
    }else{
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(text = stringResource(id = R.string.no_orders))
        }
    }
}