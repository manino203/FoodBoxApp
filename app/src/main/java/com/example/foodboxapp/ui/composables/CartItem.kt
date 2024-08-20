package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.data_holders.Product
import com.example.foodboxapp.backend.data_holders.Store

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
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImageWithLoading(
            imageUrl = item.product.imageUrl ?: "",
            modifier = Modifier.weight(0.4f),
            contentScale = ContentScale.Crop,
            contentDescription = item.product.title,
            width = 200.dp,
            height = 200.dp
        )
        Column(
            Modifier
                .weight(0.7f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.product.title,
                fontWeight = FontWeight.Bold
            )
            Price(
                item.totalPrice,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                actionChangeItemQuantity?.let{ action ->
                    var tfValue by remember(item.count) {
                        mutableStateOf("${item.count}")
                    }
                    val openDialog = remember { mutableStateOf(false) }
                    if (openDialog.value) {
                        RemoveDialog(
                            openDialog = openDialog,
                            actionConfirm = {
                                action(0)
                            }
                        ) {
                            action(1)
                        }
                    }

                    ProductCount(
                        count = item.count,
                        textFieldValue = tfValue,
                        actionChangeTextFieldValue = { tfValue = it }
                    ) {
                        if (it != 0){
                            action(it)
                        }else{
                            openDialog.value = true
                        }
                    }
                } ?: Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${item.count}x",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
            }
        }

    }
}

@Composable
fun RemoveDialog(
    openDialog: MutableState<Boolean>,
    actionConfirm: () -> Unit,
    actionDismiss: () -> Unit,
){
    AlertDialog(
        onDismissRequest = {
            actionDismiss()
            openDialog.value = false
        },
        confirmButton = {
            Button(
                onClick = {
                    actionConfirm()
                    openDialog.value = false
                }
            ) {
                Text(stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    actionDismiss()
                    openDialog.value = false
                }
            ) {
                Text(stringResource(id = R.string.no))
            }
        },
        title = {
            Text(stringResource(id = R.string.remove_item))
        },
        text = {
            Text(stringResource(id = R.string.item_removal))
        }
    )
}

@Preview
@Composable
private fun CartItemPreview(){
    CartItemComposable(
        item = CartItem(
            Product(
                "",
                "",
                "Danone Activia jogurtový nápoj",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.S-5MUqYKE5YbKZaKOE0hHwAAAA%26pid%3DApi&f=1&ipt=69ef73c62550cdf6f628378607bd5b84b09bad6956c21ebd699f54e92f12ac15&ipo=images",
                1.23f
                ),
            2,
            Store(
                null,
                null,
                "Billa",
                ""
            )
        )
    )
}

