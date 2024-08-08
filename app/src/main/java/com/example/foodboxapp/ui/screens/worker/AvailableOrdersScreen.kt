package com.example.foodboxapp.ui.screens.worker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.ui.composables.AddressComposable
import com.example.foodboxapp.ui.composables.CheckoutBar
import com.example.foodboxapp.ui.composables.OrderItemWithBottomSheet
import com.example.foodboxapp.ui.composables.OrderSummary
import com.example.foodboxapp.ui.composables.RefreshableScreen
import com.example.foodboxapp.ui.composables.ShowErrorToast
import com.example.foodboxapp.ui.composables.UiStateError
import com.example.foodboxapp.ui.composables.updateToolbarLoading
import com.example.foodboxapp.ui.composables.updateToolbarTitle
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
    updateToolbarTitle(toolbarViewModel, title)
    updateToolbarLoading(toolbarViewModel, viewModel.uiState.value.loading)

    LaunchedEffect(viewModel) {
        viewModel.collectChanges()
    }

    AvailableOrdersScreen(
        uiState = viewModel.uiState.value,
        actionRefresh = {
            viewModel.refresh()
        }
    ) {
        viewModel.acceptOrder(it)
    }

}

@Composable
private fun AvailableOrdersScreen(
    uiState: AvailableOrdersUiState,
    actionRefresh: () -> Unit,
    actionAcceptOrder: (Order) -> Unit
){
    OrderListScreen(
        isEmpty = uiState.orders.isEmpty(),
        isRefreshing = uiState.refreshing,
        actionRefresh = { actionRefresh() },
        error = uiState.error
    ){
        items(uiState.orders) {
            OrderItemWithBottomSheet(order = it) {
                actionAcceptOrder(it)
            }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderListScreen(
    isRefreshing: Boolean,
    isEmpty: Boolean,
    actionRefresh: () -> Unit,
    error: UiStateError? = null,
    items: LazyListScope.() -> Unit
){
    ShowErrorToast(error = error)
    RefreshableScreen(isRefreshing = isRefreshing, isEmpty = isEmpty, actionRefresh = actionRefresh){
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            items()
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }


}

