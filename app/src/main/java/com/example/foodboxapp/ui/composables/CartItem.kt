package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodboxapp.backend.data_holders.CartItem

@Composable
fun CartItemComposable(
    item: CartItem,
    modifier: Modifier = Modifier,
    actionDeleteItem: (() -> Unit)? = null,
    actionChangeItemQuantity: ((Int) -> Unit)? = null,
    actionClick: (() -> Unit)? = null
){
    actionClick?.let{
        Card(
            modifier = modifier,
            onClick = actionClick,
            elevation = CardDefaults.elevatedCardElevation(),
        ) {
            CartItemContent(
                item,
                actionDeleteItem,
                actionChangeItemQuantity
            )
        }
    } ?:
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(),
    ){
        CartItemContent(
            item,
            actionDeleteItem,
            actionChangeItemQuantity
            )
    }
}

@Composable
private fun CartItemContent(
    item: CartItem,
    actionDeleteItem: (() -> Unit)? = null,
    actionChangeItemQuantity: ((Int) -> Unit)? = null
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
                    actionChangeItemQuantity?.let{
                        var tfValue by remember(item.quantity) {
                            mutableStateOf("${item.quantity}")
                        }
                        ProductCount(
                            count = item.quantity,
                            textFieldValue = tfValue,
                            actionChangeTextFieldValue = { tfValue = it }
                        ) {
                            it(it)
                        }
                    } ?: Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "${item.quantity}x",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
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
        actionDeleteItem?.let{
            IconButton(onClick = it) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

