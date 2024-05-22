package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodboxapp.R
import com.example.foodboxapp.ui.composables.FoodBoxTheme
import com.example.foodboxapp.viewmodels.ToolbarViewModel

@Composable
fun SplashScreen() {
    Surface {
        Box(
            Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .requiredSize(108.dp, 108.dp)
                    .align(Alignment.Center)

            )

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .requiredSize(108.dp, 108.dp)
                    .align(Alignment.Center)

            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() = FoodBoxTheme {
    SplashScreen()
}
