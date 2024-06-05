package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    showSheet: MutableState<Boolean>,
    sheetState: SheetState,
    coroScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable (dismiss: () -> Unit) -> Unit
) {
    val dismiss: () -> Unit = {
        coroScope.launch{
            sheetState.hide()
        }.invokeOnCompletion {
            showSheet.value = false
        }
    }

    if(showSheet.value){
        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            onDismissRequest = dismiss
        ) {
            Column(
                Modifier.padding(16.dp)
            ){
                content(dismiss)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun SheetState.open(showSheet: MutableState<Boolean>, scope: CoroutineScope){
    showSheet.value = true
    scope.launch {
        show()
    }
}