package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodboxapp.backend.Product
import com.example.foodboxapp.backend.Store
import com.example.foodboxapp.viewmodels.ProductUiState
import com.example.foodboxapp.viewmodels.ProductViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductScreen(
    store: Store,
    toolbarViewModel: ToolbarViewModel
) {
    val viewModel: ProductViewModel = koinViewModel()
    LaunchedEffect(store.title) {
        toolbarViewModel.updateTitle(store.title)
    }
    LaunchedEffect(Unit) {
        viewModel.loadProducts(store)
        toolbarViewModel.updateLoading(false)
    }

    ProductScreen(viewModel.uiState.value)

}

@Composable
private fun ProductScreen(
    uiState: ProductUiState
){
    val listState = rememberLazyListState()


    LazyColumn(
        Modifier
            .fillMaxSize(),
        listState,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(uiState.products){
            ProductItem(product = it)
        }
    }
}

@Composable
private fun ProductItem(
    product: Product
) {
    Card(
        modifier = Modifier
            .padding(16.dp, 0.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.weight(0.5f),
                model = product.image,
                contentDescription = product.title
            )
            Text(
                modifier = Modifier.weight(1f),
                text = product.title
            )
            Text(
                modifier = Modifier.weight(0.5f),
                text = "${product.price}$"
            )
        }
    }
}

@Composable
@Preview
private fun ProductScreenPreview(){

}