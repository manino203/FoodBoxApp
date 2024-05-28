package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.foodboxapp.R
import com.example.foodboxapp.viewmodels.ToolbarViewModel

@Composable
fun OrderSentScreen(
    toolbarViewModel: ToolbarViewModel
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = stringResource(id = R.string.order_sent_message),
            textAlign = TextAlign.Center
        )
    }
}