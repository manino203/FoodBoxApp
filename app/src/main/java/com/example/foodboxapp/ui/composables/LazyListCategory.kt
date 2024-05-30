package com.example.foodboxapp.ui.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Suppress("FunctionName")
fun LazyListScope.Category(
    @StringRes title: Int,
    showDivider: Boolean = true,
    content: @Composable () -> Unit
){
    item{
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(title),
            color = MaterialTheme.colorScheme.primary
        )
    }
    item{
        content()
    }
    if (showDivider){
        item {
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
        }
    }
}