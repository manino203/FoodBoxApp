package com.example.foodboxapp.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.CartItem
import com.example.foodboxapp.backend.repositories.Product


@Suppress("FunctionName")
fun LazyListScope.OrderSummary(
    items: List<Pair<String, List<CartItem>>>,
    contentOnTopOfItem: @Composable (CartItem) -> Unit = {},
    showDivider: Boolean = false,
    actionChangeItemCount: ((Product, Int) -> Unit)? = null,
    actionItemDelete: ((CartItem) -> Unit)? = null,
    actionItemClick: ((CartItem) -> Unit)? = null,
) {

    Category(R.string.summary, items, showDivider){
        Column(
            Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text("${it.first}: ")
            it.second.forEach {item ->
                Box(contentAlignment = Alignment.Center){
                    CartItemComposable(
                        item = item,
                        modifier = Modifier.zIndex(0f),
                        actionDeleteItem = actionItemDelete?.let { { it(item) } },
                        actionChangeItemQuantity = actionChangeItemCount?.let {
                            {
                                it(
                                    item.product,
                                    it
                                )
                            }
                        },
                        actionClick = actionItemClick?.let { { it(item) } }
                    )
                    contentOnTopOfItem(item)
                }
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun rememberSummaryCategories(
    cartItems: List<CartItem>
): MutableState<List<Pair<String, List<CartItem>>>>{
    return remember(cartItems) {
        mutableStateOf(cartItems.map{ it.store }.toSet().map{ store ->
            Pair(store.title, cartItems.filter { store.title == it.store.title })
        })
    }
}