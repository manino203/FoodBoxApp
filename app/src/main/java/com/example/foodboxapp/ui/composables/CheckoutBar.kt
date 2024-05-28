package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodboxapp.R

@Composable
fun CheckoutBar(
    total: Float,
    buttonTitle: String,
    actionCheckout: () -> Unit
){
    Column(
        Modifier
            .fillMaxWidth()
    ){
        HorizontalDivider(Modifier.fillMaxWidth())
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Text(
                    text = "${stringResource(id = R.string.cart_total)} : ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Price(
                    total,
                    fontSize = 20.sp
                )
            }

            Button(
                modifier = Modifier.padding(16.dp, 8.dp),
                shape = RoundedCornerShape(50),
                enabled = total > 0,
                onClick = actionCheckout
            ) {
                Text(
                    text = buttonTitle
                )
            }
        }
    }
}