package com.example.foodboxapp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.data_holders.Address


@Composable
fun AddressComposable(address: Address){
    AddressComposable(street = address.street, city = address.city, zipCode = address.zipCode, country = address.country)
}
@Composable
fun AddressComposable(
    street: String,
    city: String,
    zipCode: String,
    country: String,
){
    Column{
        Text(
            text = stringResource(id = R.string.delivery_address),
            color = MaterialTheme.colorScheme.primary
        )
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = street)
            Text(text = city)
            Text(text = zipCode)
            Text(text = country)
        }
        HorizontalDivider()
    }
}

