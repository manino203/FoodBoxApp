package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage

@Composable
fun AsyncImageWithLoading(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp,
    contentScale: ContentScale = ContentScale.Fit
) {
    var loading by remember {
        mutableStateOf(true)
    }

    Box(modifier = modifier.width(width).height(height)) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = modifier.width(width).height(height),
            contentScale = contentScale,
            onLoading = {
                loading = true
            },
            onSuccess = {
                loading = false
            }
        )
        if (loading) {
            CenteredLoading(Modifier.width(width).height(height))
        }
    }
}