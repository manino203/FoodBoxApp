package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.viewmodels.PaymentMethod

@Composable
fun PaymentMethodSelector(
    selected: (PaymentMethod) -> Boolean,
    actionChangePaymentMethod: (PaymentMethod) -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PaymentMethod.methods().forEach {
            Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { actionChangePaymentMethod(it) }
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        stringResource(id = it.title).also { title ->
                            Image(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(id = it.image),
                                contentDescription = title
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = title
                            )
                        }
                    }
                    RadioButton(
                        selected = selected(it),
                        onClick = { actionChangePaymentMethod(it) })
                }
            }
        }
    }
}
