package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.ui.composables.AsyncImageWithLoading
import com.example.foodboxapp.ui.composables.BottomSheet
import com.example.foodboxapp.ui.composables.Price
import com.example.foodboxapp.ui.composables.ProductCount
import com.example.foodboxapp.ui.composables.open
import com.example.foodboxapp.ui.composables.updateToolbarLoading
import com.example.foodboxapp.ui.composables.updateToolbarTitle
import com.example.foodboxapp.viewmodels.ProductUiState
import com.example.foodboxapp.viewmodels.ProductViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductScreen(
    storeId: String,
    toolbarViewModel: ToolbarViewModel
) {
    val viewModel: ProductViewModel = koinViewModel()

    updateToolbarTitle(toolbarViewModel, viewModel.uiState.value.title ?: "")
    updateToolbarLoading(toolbarViewModel, viewModel.uiState.value.loading)

    LaunchedEffect(Unit) {
        viewModel.loadProducts(storeId)
    }



    ProductScreen(viewModel.uiState.value){ product, quantity ->
        viewModel.addProductToCart(product, quantity, storeId)
    }

}

@Composable
private fun ProductScreen(
    uiState: ProductUiState,
    actionAddToCart: (Product, Int) -> Unit
){
    val listState = rememberLazyListState()

    LazyColumn(
        Modifier
            .fillMaxSize(),
        listState,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(uiState.products){
            ProductItem(product = it, actionAddToCart)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductItem(
    product: Product,
    actionAddToCart: (Product, Int) -> Unit
) {

    val sheetOpen = remember{
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true, )
    val coroScope = rememberCoroutineScope()

    AddToCartSheet(
        product = product,
        sheetState = sheetState,
        sheetOpen = sheetOpen,
        coroScope = coroScope
    ) {
        actionAddToCart(product, it)
    }

    Card(
        modifier = Modifier
            .padding(16.dp, 0.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                sheetState.open(sheetOpen, coroScope)
            },
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImageWithLoading(
                    modifier = Modifier.weight(0.5f),
                    imageUrl = product.imageUrl ?: "",
                    contentDescription = product.title,
                    width = 100.dp,
                    height = 100.dp
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = product.title
                )
                Price(
                    product.price,
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddToCartSheet(
    product: Product,
    sheetOpen: MutableState<Boolean>,
    sheetState: SheetState,
    coroScope: CoroutineScope,
    actionAdd: (Int) -> Unit
){
    var quantity by remember {
       mutableIntStateOf(1)
    }

    var tfValue by remember(quantity) {
        mutableStateOf("$quantity")
    }
    BottomSheet(
        modifier = Modifier.wrapContentHeight(),
        sheetState = sheetState,
        showSheet = sheetOpen,
        coroScope = coroScope
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title
            )
            Text(text = product.title)
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
                contentAlignment = Alignment.Center) {
                Text(text = "Details")
            }
            Price(
                price = product.price,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                Modifier.padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                ProductCount(
                    Modifier.weight(.4f),
                    count = quantity,
                    textFieldValue = tfValue,
                    actionChangeTextFieldValue = {tfValue = it}
                ) {
                    quantity = it
                }
                Row(Modifier.weight(0.2f)) {

                }
                Button(
                    modifier = Modifier
                        .weight(0.4f),
                    enabled = tfValue.isNotBlank() && quantity > 0,
                    onClick = {
                        actionAdd(quantity)
                        it()
                    }
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = stringResource(id = R.string.add_to_cart))
                    Text(text = stringResource(id = R.string.add_to_cart))
                }
            }
        }
    }
}
