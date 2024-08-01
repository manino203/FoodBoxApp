package com.example.foodboxapp.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodboxapp.R
import com.example.foodboxapp.navigation.ScreenDestination
import com.example.foodboxapp.viewmodels.ToolbarUiState
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popUpTo


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Toolbar(
    uiState: ToolbarUiState,
    navController: NavController<ScreenDestination>,
    actionOpenMenu: () -> Unit
){
    val currentScreen by remember(navController.backstack.entries.last()) {
        mutableStateOf(navController.backstack.entries.last().destination)
    }
    val cartScreens = listOf(ScreenDestination.Cart, ScreenDestination.Checkout, ScreenDestination.OrderSent)
    val backArrowScreens = listOf(ScreenDestination.Checkout)
    if(uiState.visible){
        Column {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text(uiState.title) },
                navigationIcon = {
                    if(!backArrowScreens.contains(currentScreen)){
                        IconButton(onClick = actionOpenMenu) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_menu),
                                contentDescription = "Menu"
                            )
                        }
                    }else{
                        IconButton(onClick = { navController.pop() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Menu"
                            )
                        }
                    }
                },
                actions = {
                    if(uiState.showActions){
                        if (!cartScreens.contains(currentScreen)) {
                            IconButton(onClick = {
                                navController.navigate(ScreenDestination.Cart)
                            }) {
                                Icon(
                                    Icons.Outlined.ShoppingCart,
                                    contentDescription = stringResource(id = R.string.cart_screen_title)
                                )
                                if (uiState.cartItemCount > 0) {
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
                    } else {
                            IconButton(onClick = {
                                navController.popUpTo {
                                    !cartScreens.contains(it)
                                }
                            }) {
                                Icon(
                                    Icons.Filled.Home,
                                    contentDescription = stringResource(id = R.string.cart_screen_title)
                                )
                            }

                    }
                }}
            )
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .alpha(if (uiState.loading) 1f else 0f)
            )
        }
    }
}


@SuppressLint("ComposableNaming")
@Composable
fun updateToolbarLoading(
    vm: ToolbarViewModel,
    loading: Boolean
){
    LaunchedEffect(loading) {
        vm.updateLoading(loading)
    }
}


@SuppressLint("ComposableNaming")
@Composable
fun updateToolbarTitle(
    vm: ToolbarViewModel,
    title: String
){
    LaunchedEffect(title) {
        vm.updateTitle(title)
    }
}