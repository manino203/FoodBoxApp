package com.example.foodboxapp.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R

@Composable
fun NavigationDrawer(
    actionClose: () -> Unit
) {
    BoxWithConstraints {
        ModalDrawerSheet(
            Modifier
                .height(maxHeight)
                .verticalScroll(rememberScrollState())) {
            IconButton(actionClose) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp, 15.dp, 10.dp, 10.dp)
                        .requiredSize(32.dp)
                )
            }
            Header(image = R.drawable.ic_launcher_foreground, title = "Account", subtitle = "Client")
            MenuItem(label = stringResource(id = R.string.new_order), selected = false, onClick = { /*TODO*/ }, icon ={
                Icon(Icons.Default.Add, contentDescription = "Add")
            })
            MenuItem(label = stringResource(id = R.string.order_history), selected = false, onClick = { /*TODO*/ }, icon = {
                Icon(painter = painterResource(id = R.drawable.history), contentDescription = "App Settings")
            })
            MenuItem(label = stringResource(id = R.string.acc_stats), selected = false, onClick = { /*TODO*/ }, icon = {
                Icon(painter = painterResource(id = R.drawable.bar_chart), contentDescription = "App Settings")
            })
            Spacer(modifier = Modifier.weight(1f))
            Column {
                MenuItem(label = stringResource(id = R.string.app_settings), selected = false, onClick = { /*TODO*/ }, icon = {
                    Icon(Icons.Default.Settings, contentDescription = "App Settings")
                })
                MenuItem(label = stringResource(id = R.string.log_out), selected = false, onClick = { /*TODO*/ }, icon = {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Log Out")
                })
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
    Row(modifier = modifier
        .padding(vertical = 10.dp)
        .wrapContentHeight()
        .wrapContentWidth(Alignment.Start)

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