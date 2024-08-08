package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Order
import com.example.foodboxapp.ui.screens.worker.OrderDetailsSheet
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrderItem(
    order: Order,
    actionClick: () -> Unit
){
    Card(
        actionClick,
        Modifier.fillMaxWidth()
    ){
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(text = "${stringResource(id = R.string.order_date)}: ${
                order.timestamp?.seconds?.let {
                    SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(it * 1000)
                } ?: ""
            }")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderItemWithBottomSheet(
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
    OrderItem(order = order) {
        sheetState.open(sheetOpen, coroScope)
    }
}