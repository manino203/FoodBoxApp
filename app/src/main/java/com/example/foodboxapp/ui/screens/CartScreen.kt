package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.repositories.dummyStoreList
import com.example.foodboxapp.ui.composables.CheckoutBar
import com.example.foodboxapp.ui.composables.OrderSummary
import com.example.foodboxapp.ui.composables.rememberSummaryCategories
import com.example.foodboxapp.viewmodels.CartUiState
import com.example.foodboxapp.viewmodels.CartViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartScreen(
    toolbarViewModel: ToolbarViewModel,
    actionCheckout: () -> Unit
) {
    val viewModel: CartViewModel = koinViewModel()
    val title = stringResource(id = R.string.cart_screen_title)
    LaunchedEffect(title) {
        toolbarViewModel.updateTitle(title)
    }

    LaunchedEffect(Unit) {
        viewModel.collectCartChanges()
        toolbarViewModel.updateLoading(false)
    }

    CartScreen(
        viewModel.uiState.value,
        actionCheckout = {
            actionCheckout()
        },
        actionDeleteItem = {
            viewModel.deleteItem(it)
        }
    ){ product, count ->
        viewModel.changeQuantity(product, count)
    }
}

@Composable
private fun CartScreen(
    uiState: CartUiState,
    actionCheckout: () -> Unit,
    actionDeleteItem: (CartItem) -> Unit,
    actionChangeItemQuantity: (Product, Int) -> Unit
){

    val summaryCategories by rememberSummaryCategories(cartItems = uiState.items)

    Scaffold(
        Modifier.fillMaxSize(),
        bottomBar = {
            CheckoutBar(uiState.totalPrice, stringResource(id = R.string.checkout)) {
                actionCheckout()
            }
        }
    ){ padding ->

        if (uiState.items.isNotEmpty()){
            LazyColumn(
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OrderSummary(
                    items = summaryCategories,
                    actionItemDelete = { actionDeleteItem(it) },
                    actionChangeItemCount = { product, count ->
                        actionChangeItemQuantity(product, count)
                    }
                )
            }
        }else{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ){
                Text(stringResource(id = R.string.cart_empty))
            }
        }
    }
}

@Preview
@Composable
private fun CartScreenPreview(){
    CartScreen(uiState = CartUiState(items = listOf(
        CartItem(
            Product(
                "Tesco Finest Clementine Or Sweet Easy Peeler 600G",
                "https://digitalcontent.api.tesco.com/v2/media/ghs/cfb3c09d-511a-4982-b329-f0d1793ad5e0/ac881f1a-3e3d-4c5f-a316-68da6226a708.jpeg?h=225&w=225",
                2.5f
            ),
            1,
            dummyStoreList[0],
            2.50f
    )
    )),{}, {}) { _, _ -> }
}