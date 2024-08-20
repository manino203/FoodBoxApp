package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

@Composable
fun ProductCount(
    modifier: Modifier = Modifier,
    count: Int,
    minusButtonEnabled: Boolean = true,
    textFieldValue: String,
    actionChangeTextFieldValue: (String) -> Unit,
    actionChangeCount: (Int) -> Unit
){
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Button(
            modifier = Modifier
                .weight(.3f)
                .alpha(0.8f),
            contentPadding = PaddingValues(0.dp),
            enabled = minusButtonEnabled,
            onClick = { actionChangeCount(count - 1) },
        ) {
            Text(text = "-")
        }
        TextField(
            modifier = Modifier
                .weight(.5f)
                .wrapContentWidth(),
            value = textFieldValue,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background
            ),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            shape = RoundedCornerShape(50),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { value ->
                if (value.isDigitsOnly()) {
                    if (value.isNotBlank()){
                        actionChangeCount(value.toInt())
                    }else{
                        actionChangeTextFieldValue(value)
                    }

                }

            }
        )
        Button(
            modifier = Modifier
                .weight(.3f)
                .alpha(0.8f),
            contentPadding = PaddingValues(0.dp),
            onClick = { actionChangeCount(count + 1) }
        ) {
            Text(text = "+")
        }
    }
}