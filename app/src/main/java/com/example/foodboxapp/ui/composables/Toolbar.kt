package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodboxapp.R
import com.example.foodboxapp.viewmodels.Action
import com.example.foodboxapp.viewmodels.ToolbarUiState


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Toolbar(
    uiState: ToolbarUiState,
    actionCart: () -> Unit,
    actionHome: () -> Unit,
    actionOpenMenu: () -> Unit
){
    if(uiState.visible){
        Column {
            TopAppBar(
                title = { Text(uiState.title) },
                navigationIcon = {
                    IconButton(onClick = actionOpenMenu) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    if (uiState.action == Action.CART) {
                        IconButton(onClick = actionCart) {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = stringResource(id = R.string.cart_screen_title))
                            if(uiState.cartItemCount > 0){
                                Text(
                                    text = "${uiState.cartItemCount}",
                                    modifier = Modifier
                                        .height(16.dp)
                                        .offset(8.dp, (-8).dp)
                                        .background(
                                            MaterialTheme.colorScheme.error,
                                            shape = CircleShape
                                        )
                                        .padding(horizontal = 5.dp),
                                    color = MaterialTheme.colorScheme.onError,
                                    lineHeight = 18.sp,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }else{
                        IconButton(onClick = actionHome) {
                            Icon(Icons.Filled.Home, contentDescription = stringResource(id = R.string.cart_screen_title))
                        }

                    }
                }
            )
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .alpha(if (uiState.loading) 1f else 0f)
            )
        }
    }
}