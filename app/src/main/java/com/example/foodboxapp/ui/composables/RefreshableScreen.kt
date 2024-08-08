package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.foodboxapp.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RefreshableScreen(
    isRefreshing: Boolean,
    isEmpty: Boolean,
    emptyMessage: String = stringResource(id = R.string.no_orders),
    actionRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = { actionRefresh()})
    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        contentAlignment = Alignment.Center
    ){
        content()
        PullRefreshIndicator(refreshing = isRefreshing, state = refreshState, modifier = Modifier.align(
            Alignment.TopCenter))
        if(isEmpty){
            Text(text = emptyMessage)
        }
    }
}