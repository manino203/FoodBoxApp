package com.example.foodboxapp.ui.composables

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.backend.network.LocalizedException


@Composable
fun ErrorBox(
    message: String, dismissible: Boolean = false
) {
    var isDismissed by remember(message, dismissible) { mutableStateOf(false) }

    AnimatedVisibility(!isDismissed) {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(start = 20.dp, end = 10.dp).fillMaxWidth()
            ) {
                Text(message, Modifier.padding(vertical = 20.dp), fontWeight = FontWeight.Medium)

                if (dismissible) {
                    IconButton(onClick = { isDismissed = true }, Modifier.wrapContentSize()) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun AutoErrorBox(error: UiStateError?, dismissible: Boolean = false) {
    AutoErrorBox(error?.localize(), dismissible)
}

@Composable
fun AutoErrorBox(message: String?, dismissible: Boolean = false) {
    AnimatedVisibility(message != null) {
        ErrorBox(message.orEmpty(), dismissible)
    }
}


data class UiStateError(
    val exception: Throwable
)


@Composable
fun UiStateError.localize(): String {
    val context = LocalContext.current
    return remember(exception, context) {
        if (exception is LocalizedException) {
            Log.d("Exception", "${exception.originalException}: ${exception.originalException?.message}")
            context.resources.getString(exception.messageId)
        }else{
            Log.d("Exception", "$exception: ${exception.message}")
            exception.message ?: ""
        }
    }
}