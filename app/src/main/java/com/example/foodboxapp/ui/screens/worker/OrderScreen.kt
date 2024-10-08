package com.example.foodboxapp.ui.screens.worker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.ui.composables.AddressComposable
import com.example.foodboxapp.ui.composables.CenteredLoading
import com.example.foodboxapp.ui.composables.OrderSummary
import com.example.foodboxapp.ui.composables.ShowErrorToast
import com.example.foodboxapp.ui.composables.rememberSummaryCategories
import com.example.foodboxapp.ui.composables.updateToolbarLoading
import com.example.foodboxapp.ui.composables.updateToolbarTitle
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import com.example.foodboxapp.viewmodels.worker.OrderUiState
import com.example.foodboxapp.viewmodels.worker.OrderViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderScreen(
    toolbarViewModel: ToolbarViewModel,
    orderId: String,
    actionCompleteOrder: () -> Unit
) {
    val viewModel: OrderViewModel = koinViewModel()
    val title = stringResource(id = R.string.order)
    updateToolbarLoading(toolbarViewModel, viewModel.uiState.value.loading)
    updateToolbarTitle(toolbarViewModel, title)

    LaunchedEffect(viewModel) {
        viewModel.update(orderId)
    }

    val order by remember(viewModel.uiState.value.order) {
        mutableStateOf(viewModel.uiState.value.order)
    }

    order?.let{o ->
        OrderScreen(viewModel.uiState.value, o, {
            viewModel.crossOutItem(it)
        }) {
            viewModel.completeOrder(o) {
                actionCompleteOrder()
            }
        }
    }?: CenteredLoading()
}

@Composable
private fun OrderScreen(
    uiState: OrderUiState,
    order: Order,
    actionClickItem: (CartItem) -> Unit,
    actionCompleteOrder: () -> Unit
){

    val summary by rememberSummaryCategories(order.items)

    ShowErrorToast(error = uiState.error)

    Column(Modifier.fillMaxSize()) {
        AddressComposable(order.address)
        LazyColumn(Modifier.fillMaxWidth()) {
            OrderSummary(
                items = summary,
                contentOnTopOfItem = {
                    if(uiState.crossedOutItems.contains(it)){
                        HorizontalDivider(Modifier.padding(8.dp), color = Color.Red, thickness = 3.dp)
                    }
                }
            ){
                actionClickItem(it)
            }
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = actionCompleteOrder) {
                        Text(text = stringResource(id = R.string.complete_order))
                    }
                }
            }
        }
    }
}