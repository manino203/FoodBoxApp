package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Store
import com.example.foodboxapp.ui.composables.AsyncImageWithLoading
import com.example.foodboxapp.ui.composables.RefreshableScreen
import com.example.foodboxapp.ui.composables.ShowErrorToast
import com.example.foodboxapp.ui.composables.updateToolbarLoading
import com.example.foodboxapp.ui.composables.updateToolbarTitle
import com.example.foodboxapp.viewmodels.StoreUiState
import com.example.foodboxapp.viewmodels.StoreViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StoreScreen(
    toolbarViewModel: ToolbarViewModel,
    actionNavigateToStoreScreen: (String) -> Unit
) {
    val viewModel: StoreViewModel = koinViewModel()
    val title = stringResource(id = R.string.app_name)

    LaunchedEffect(Unit) {
        viewModel.getStores()
        viewModel.collectChanges()
    }
    updateToolbarTitle(toolbarViewModel, title)
    updateToolbarLoading(toolbarViewModel, viewModel.uiState.value.loading)

    StoreScreen(
        viewModel.uiState.value,
        {viewModel.refresh()}
    ){
        actionNavigateToStoreScreen(it.id)
    }
}

@Composable
private fun StoreScreen(
    uiState: StoreUiState,
    actionRefresh: () -> Unit,
    actionStoreClick: (Store) -> Unit
){
    val listState = rememberLazyListState()

    ShowErrorToast(error = uiState.error)

    RefreshableScreen(
        isRefreshing = uiState.isRefreshing,
        isEmpty = uiState.storeList.isEmpty(),
        emptyMessage = stringResource(id = R.string.nothing_here),
        actionRefresh = actionRefresh
    ){
    LazyColumn(
        Modifier
            .fillMaxSize(),
        listState,
    ) {
        items(uiState.storeList) {
            StoreItem(
                it
            ){
                actionStoreClick(it)
            }
        }
    }
    }


}

@Composable
fun StoreItem(
    store: Store,
    actionClick: () -> Unit
){
    Card(
        modifier = Modifier
            .padding(16.dp, 8.dp)
            .clickable {
                actionClick()
            }
        ,
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AsyncImageWithLoading(
                modifier = Modifier.requiredSize(96.dp),
                imageUrl = store.imageUrl ?: "",
                contentDescription = store.title,
                width = 100.dp,
                height = 100.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = store.title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            )
            store.address?.let {
                Text(text = buildString {
                    append(it.street)
                    append(", ")
                    append(it.zipCode)
                    append(", ")
                    append(it.city)
                    append(", ")
                    append(it.country)
                })
            }
        }
    }
}

@Composable
@Preview
private fun HomeScreenPreview(){
    val vm: ToolbarViewModel = koinViewModel()
    StoreScreen(toolbarViewModel = vm){}
}