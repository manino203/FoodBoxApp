package com.example.foodboxapp.ui.screens.worker

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.ui.composables.OrderItem
import com.example.foodboxapp.ui.composables.updateToolbarLoading
import com.example.foodboxapp.ui.composables.updateToolbarTitle
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import com.example.foodboxapp.viewmodels.worker.AcceptedOrdersUiState
import com.example.foodboxapp.viewmodels.worker.AcceptedOrdersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AcceptedOrdersScreen(
    toolbarViewModel: ToolbarViewModel,
    actionNavToOrder: (String) -> Unit
) {
    val viewModel: AcceptedOrdersViewModel = koinViewModel()
    val title = stringResource(id = R.string.accepted_orders)

    updateToolbarTitle(toolbarViewModel, title)
    updateToolbarLoading(toolbarViewModel, viewModel.uiState.value.loading)

    LaunchedEffect(Unit) {
        viewModel.collectChanges()
    }

    AcceptedOrdersScreen(uiState = viewModel.uiState.value, { viewModel.refresh() } ){
        actionNavToOrder(it.id)
    }

}


@Composable
private fun AcceptedOrdersScreen(
    uiState: AcceptedOrdersUiState,
    actionRefresh: () -> Unit,
    actionClickOrder: (Order) -> Unit
){


    OrderListScreen(
        isRefreshing = uiState.refreshing,
        isEmpty = uiState.orders.isEmpty(),
        actionRefresh = actionRefresh,
        error = uiState.error
    ){
        items(uiState.orders) {
            OrderItem(order = it) {
                actionClickOrder(it)
            }
        }
    }


}