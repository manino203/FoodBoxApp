package com.example.foodboxapp.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


private val FoodBoxDarkBlue = Color(0xff0071BC)
private val FoodBoxBlue = Color(0xff1B93E2)

// ...

private val darkColorScheme = darkColorScheme(
    primary = FoodBoxBlue,
    onPrimary = Color.White,
    secondary = FoodBoxDarkBlue,
    onSecondary = Color.White
)
private val lightColorScheme = lightColorScheme(
    primary = FoodBoxBlue,
    onPrimary = Color.White,
    secondary = FoodBoxDarkBlue,
    onSecondary = Color.White,
    surfaceVariant = Color(0xffDEF1FA)
)

@Composable
private fun Palette() {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.onSecondary,
        MaterialTheme.colorScheme.onBackground,
        MaterialTheme.colorScheme.onSurface,
        MaterialTheme.colorScheme.onError,
    )

    val showColor: @Composable (String, Color, Color) -> Unit = { label, fg, bg ->
        Row(Modifier.background(bg)) {
            Text(text=label, modifier= Modifier.padding(10.dp), color=fg)
        }
    }

    Column {
        Row { colors.forEach { Box(modifier = Modifier.size(50.dp).background(it)) } }
        showColor("Surface Text", MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.surface)
        showColor("Error Message", MaterialTheme.colorScheme.onError, MaterialTheme.colorScheme.error)
        showColor("Primary text", MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.primary)
        showColor("Background text", MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.background)
        showColor("Secondary text", MaterialTheme.colorScheme.onSecondary, MaterialTheme.colorScheme.secondary)
    }

}

@Preview(name = "small font", group = "font scales", fontScale = 0.5f)
@Preview(name = "normal font", group = "font scales", fontScale = 1f)
@Preview(name = "large font", group = "font scales", fontScale = 1.5f)
annotation class FontScalePreviews

@Preview(name = "light mode", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "dark mode", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class ThemePreviews

@Preview(name = "portrait", group = "orientation", device = Devices.PIXEL, showSystemUi = true)
@Preview(name = "landscape", group = "orientation", device = Devices.AUTOMOTIVE_1024p, showSystemUi = true)
annotation class OrientationPreviews

@Preview(name = "en", group = "locales", locale = "en")
@Preview(name = "sk", group = "locales", locale = "sk")
@Preview(name = "cs", group = "locales", locale = "cs")
annotation class LocalePreviews

@OrientationPreviews
@ThemePreviews
@FontScalePreviews
@LocalePreviews
annotation class CombinedPreviews

@Composable
@Preview
private fun PalettePreview() {
    Column {
        FoodBoxLightTheme {
            Palette()
        }
        FoodBoxDarkTheme {
            Palette()
        }
    }
}

@Composable
fun FoodBoxLightTheme(content: @Composable () -> Unit)
        = MaterialTheme(colorScheme = lightColorScheme, content = content)

@Composable
fun FoodBoxDarkTheme(content: @Composable () -> Unit)
        = MaterialTheme(colorScheme = darkColorScheme, content = content)

@Composable
fun FoodBoxTheme(content: @Composable () -> Unit) {
    if (isSystemInDarkTheme()) {
        FoodBoxDarkTheme {
            content()
        }
    } else {
        FoodBoxLightTheme {
            content()
        }
    }
}

@Composable
fun FoodBoxThemeWithSurface(modifier: Modifier, content: @Composable () -> Unit) {
    FoodBoxTheme {
        Surface(modifier) {
            content()
        }
    }
}

@Composable
fun FoodBoxThemeWithSurface(content: @Composable () -> Unit)
        = FoodBoxThemeWithSurface(Modifier.fillMaxSize(), content)
