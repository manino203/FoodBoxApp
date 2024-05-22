package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import coil.compose.AsyncImage
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.Store
import com.example.foodboxapp.viewmodels.StoreUiState
import com.example.foodboxapp.viewmodels.StoreViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StoreScreen(
    toolbarViewModel: ToolbarViewModel,
    actionNavigateToStoreScreen: (Store) -> Unit
) {
    val viewModel: StoreViewModel = koinViewModel()
    val title = stringResource(id = R.string.app_name)
    LaunchedEffect(title) {
        toolbarViewModel.updateTitle(title)
    }
    LaunchedEffect(Unit) {
        viewModel.getStores()
        toolbarViewModel.updateLoading(false)
    }

    StoreScreen(
        viewModel.uiState.value
    ){
        actionNavigateToStoreScreen(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StoreScreen(
    uiState: StoreUiState,
    actionStoreClick: (Store) -> Unit
){
    val listState = rememberLazyListState()


    LazyColumn(
        Modifier
            .fillMaxSize(),
        listState,
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

@Composable
fun StoreItem(
    store: Store,
    actionClick: () -> Unit
){
    Card(
        modifier = Modifier
            .padding(16.dp, 0.dp)
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
            AsyncImage(
                modifier = Modifier.requiredSize(96.dp),
                model = store.image,
                contentDescription = store.title
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = store.title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            )
            Text(text = store.address)
        }
    }
}

@Composable
@Preview
private fun HomeScreenPreview(){
    StoreScreen(toolbarViewModel = ToolbarViewModel()){}
}