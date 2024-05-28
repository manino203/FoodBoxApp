package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodboxapp.backend.repositories.CartItem

@Composable
fun CartItem(
    item: CartItem,
    modifier: Modifier = Modifier,
    editable: Boolean = true,
    actionDeleteItem: () -> Unit = {},
    actionChangeItemQuantity: (Int) -> Unit = { _ -> }
){
    Card(
        modifier = modifier,
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
                        if (editable){
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
                        }else{
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "${item.quantity}x",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
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
            if (editable){
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
}