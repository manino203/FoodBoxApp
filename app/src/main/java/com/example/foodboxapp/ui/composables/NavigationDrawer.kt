package com.example.foodboxapp.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.AccountType
import com.example.foodboxapp.navigation.ScreenDestination
import com.example.foodboxapp.viewmodels.MainUiState
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate


@Composable
fun NavigationDrawer(
    uiState: MainUiState,
    navController: NavController<ScreenDestination>,
    actionLogout: () -> Unit,
    actionClose: () -> Unit
) {
    val currentDestination by remember(navController.backstack.entries.last()) {
        mutableStateOf(navController.backstack.entries.last().destination)
    }
    BoxWithConstraints {
        ModalDrawerSheet(
            Modifier
                .height(maxHeight)
                .verticalScroll(rememberScrollState())
        ) {
            IconButton(actionClose) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp, 15.dp, 10.dp, 10.dp)
                        .requiredSize(32.dp)
                )
            }
            uiState.account?.let{
                Header(
                    image = R.drawable.ic_launcher_foreground,
                    title = it.email,
                    subtitle = stringResource(it.type.title),
                    modifier = Modifier.clickable {
                        navController.navigate(ScreenDestination.AccountSettings)
                        actionClose()
                    }
                )
            }
            uiState.account?.let{
                if (uiState.account.type == AccountType.Client){
                    MenuItem(label = stringResource(id = R.string.new_order),
                        selected = currentDestination == ScreenDestination.Main,
                        onClick = {
                            navController.navigate(ScreenDestination.Main)
                            actionClose()
                        },
                        icon = {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.new_order)
                            )
                        }
                    )
                }else{
                    MenuItem(label = stringResource(id = R.string.available_orders),
                        selected = currentDestination == ScreenDestination.AvailableOrders,
                        onClick = {
                            navController.navigate(ScreenDestination.AvailableOrders)
                            actionClose()
                        }
                    )
                    MenuItem(label = stringResource(id = R.string.accepted_orders),
                        selected = currentDestination == ScreenDestination.AcceptedOrders,
                        onClick = {
                            navController.navigate(ScreenDestination.AcceptedOrders)
                            actionClose()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                MenuItem(
                    label = stringResource(R.string.settings),
                    selected = currentDestination == ScreenDestination.Settings,
                    onClick = {
                        navController.navigate(ScreenDestination.Settings)
                        actionClose()
                    },
                    icon = {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(id = R.string.settings))
                    })
                uiState.account?.let{
                    MenuItem(
                        label = stringResource(id = R.string.log_out),
                        selected = false,
                        onClick = {
                            actionLogout()
                            actionClose()
                        },
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = stringResource(
                                    id = R.string.log_out
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
) {
    NavigationDrawerItem(
        label = { Text(label) },
        selected = selected,
        onClick = onClick,
        icon = icon,
        badge = badge
    )
}

@Composable
private fun Header(
    @DrawableRes image: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 10.dp)
            .wrapContentHeight()
            .fillMaxWidth()

    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp, 60.dp)
                .padding(horizontal = 10.dp)
        )

        Column(
            modifier = Modifier
                .heightIn(min = 60.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle)
        }
    }
}