package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.CartItem
import com.example.foodboxapp.backend.Product
import com.example.foodboxapp.ui.composables.Price
import com.example.foodboxapp.viewmodels.Action
import com.example.foodboxapp.viewmodels.CartUiState
import com.example.foodboxapp.viewmodels.CartViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartScreen(
    toolbarViewModel: ToolbarViewModel
) {
    val viewModel: CartViewModel = koinViewModel()
    //todo: toolbar state
    val title = stringResource(id = R.string.cart_screen_title)
    LaunchedEffect(title) {
        toolbarViewModel.updateTitle(title)
    }

    DisposableEffect(Unit) {
        viewModel.collectCartChanges()
        toolbarViewModel.updateAction(Action.HOME)
        toolbarViewModel.updateLoading(false)
        onDispose {
            toolbarViewModel.updateAction(Action.CART)
        }
    }

    CartScreen(
        viewModel.uiState.value,
        actionCheckout = {},
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
    Scaffold(
        Modifier.fillMaxSize(),
        bottomBar = {
            CheckoutBar(uiState.totalPrice) {
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
                items(uiState.items) { item ->
                    CartItem(
                        item = item,
                        actionDeleteItem = {
                            actionDeleteItem(item)
                        }
                    ) {
                        actionChangeItemQuantity(item.product, it)
                    }
                }
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

@Composable
private fun CartItem(
    item: CartItem,
    actionDeleteItem: () -> Unit,
    actionChangeItemQuantity: (Int) -> Unit
){
    Card(
        modifier = Modifier,
        elevation = CardDefaults.elevatedCardElevation()
    ){
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                Modifier.weight(0.75f),
                verticalAlignment = Alignment.CenterVertically
            ){
                AsyncImage(
                    modifier = Modifier.weight(0.4f),
                    model = item.product.image,
                    contentDescription = item.product.title
                )
                Column(
                    Modifier.weight(0.6f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = item.product.title,
                        fontWeight = FontWeight.Bold
                    )
                    Price(
                        item.product.price,
                        color = Color.Gray
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .height(24.dp)
                                .alpha(0.8f),
                            contentPadding = PaddingValues(0.dp),
                            onClick = { actionChangeItemQuantity(item.quantity - 1) },
                        ) {
                            Text(text = "-")

                        }
                        Text(
                            text = "${item.quantity}",
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            modifier = Modifier
                                .height(24.dp)
                                .alpha(0.8f),
                            contentPadding = PaddingValues(0.dp),
                            onClick = { actionChangeItemQuantity(item.quantity + 1) }
                        ) {
                            Text(text = "+")
                        }
                    }
                }
            }
            // todo: vertical divider
            Price(
                item.totalPrice,
                modifier = Modifier.weight(0.25f),
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            IconButton(onClick = actionDeleteItem) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

}

@Composable
private fun CheckoutBar(
    total: Float,
    actionCheckout: () -> Unit
){
    Column(
        Modifier
            .fillMaxWidth()
    ){
        HorizontalDivider(Modifier.fillMaxWidth())
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Text(
                    text = "${stringResource(id = R.string.cart_total)} : ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Price(
                    total,
                    fontSize = 20.sp
                )
            }

            Button(
                modifier = Modifier.padding(16.dp, 8.dp),
                shape = RoundedCornerShape(50),
                enabled = total > 0,
                onClick = actionCheckout
            ) {
                Text(
                    text = stringResource(id = R.string.cart_checkout)
                )
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
            2.50f
    )
    )),{}, {}) { _, _ -> }
}