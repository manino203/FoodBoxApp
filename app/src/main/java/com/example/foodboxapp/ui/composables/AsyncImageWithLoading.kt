package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.example.foodboxapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    var error by remember {
        mutableStateOf(false)
    }

    val timeout = 5_000L
    val coroScope = rememberCoroutineScope()

    Box(modifier = modifier
        .width(width)
        .height(height),
        contentAlignment = Alignment.Center
        ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier
                .width(width)
                .height(height),
            contentScale = contentScale,
            onLoading = {
                loading = true
                error = false
                coroScope.launch {
                    delay(timeout)
                }.invokeOnCompletion {
                    if(loading){
                        loading = false
                        error = true
                    }
                }
            },
            onSuccess = {
                loading = false
            }
        )
        if (loading) {
            CenteredLoading(
                Modifier
                    .width(width)
                    .height(height))
        }
        if (error ){
            Text(text = stringResource(id = R.string.image_error))
        }
    }
}