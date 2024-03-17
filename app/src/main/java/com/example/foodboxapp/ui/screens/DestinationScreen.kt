package com.example.foodboxapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.foodboxapp.viewmodels.ToolbarViewModel


@Composable
fun DestinationScreen(
    toolbarViewModel: ToolbarViewModel,
    modifier: Modifier = Modifier
) {
    LoginScreen(toolbarViewModel, modifier = modifier)
}