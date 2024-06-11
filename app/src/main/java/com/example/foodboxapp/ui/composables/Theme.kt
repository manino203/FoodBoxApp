package com.example.foodboxapp.ui.composables


////private val FoodBoxGreen = Color(0xff39C300)
////private val FoodBoxOrange = Color(0xffFFC12D)
////private val FoodBoxTeal = Color(0xff05A1A5)
////
////
////
////private val darkColorScheme = darkColorScheme(
////    primary = FoodBoxGreen,
////    onPrimary = Color.White,
////    secondary = FoodBoxOrange,
////    onSecondary = Color.White,
////    tertiary = FoodBoxTeal,
////
////)
////private val lightColorScheme = lightColorScheme(
////    primary = FoodBoxGreen,
////    onPrimary = Color.White,
////    secondary = FoodBoxOrange,
////    onSecondary = Color.White,
////    tertiary = FoodBoxTeal
////)
//
//// Define your custom primary color
//val customPrimaryColor = Color(0xFF4ED622)
//
//// Define your custom color schemes
//val lightColorScheme = lightColorScheme(
//    primary = customPrimaryColor,
//    // Choose complementary colors for other roles
//    primaryContainer = Color(0xFF248D22),
//    secondary = Color(0xFFAED581),
//    secondaryContainer = Color(0xFF64B72E),
//    background = Color(0xFFFFFFFF),
//    surface = Color(0xFFFFFFFF),
//    onPrimary = Color(0xFFFFFFFF),
//    onSecondary = Color(0xFF000000),
//    onBackground = Color(0xFF000000),
//    onSurface = Color(0xFF000000),
//    onError = Color(0xFFFFFFFF),
//    errorContainer = Color(0xFF880000),
//    onPrimaryContainer = Color(0xFF000000),
//    onSecondaryContainer = Color(0xFF000000),
//    onSurfaceVariant = Color(0xFF000000),
//)
//
//val darkColorScheme = darkColorScheme(
////    primary = customPrimaryColor,
////    // Choose complementary colors for other roles
////    primaryVariant = Color(0xFF248D22),
////    secondary = Color(0xFFAED581),
////    secondaryVariant = Color(0xFF64B72E),
////    background = Color(0xFF424242),
////    surface = Color(0xFF121212),
////    onPrimary = Color(0xFFFFFFFF),
////    onSecondary = Color(0xFFFFFFFF),
////    onBackground = Color(0xFFFFFFFF),
////    onSurface = Color(0xFFFFFFFF),
////    onError = Color(0xFFFF1744),
////    onErrorVariant = Color(0xFFD50000),
////    onPrimaryVariant = Color(0xFFFFFFFF),
////    onSecondaryVariant = Color(0xFFFFFFFF),
////    onBackgroundVariant = Color(0xFFFFFFFF),
////    onSurfaceVariant = Color(0xFFFFFFFF),
////    onErrorVariant = Color(0xFFFFFFFF)
//)
//
//@Composable
//private fun Palette() {
//    val colors = listOf(
//        MaterialTheme.colorScheme.primary,
//        MaterialTheme.colorScheme.secondary,
//        MaterialTheme.colorScheme.background,
//        MaterialTheme.colorScheme.surface,
//        MaterialTheme.colorScheme.error,
//        MaterialTheme.colorScheme.onPrimary,
//        MaterialTheme.colorScheme.onSecondary,
//        MaterialTheme.colorScheme.onBackground,
//        MaterialTheme.colorScheme.onSurface,
//        MaterialTheme.colorScheme.onError,
//    )
//
//    val showColor: @Composable (String, Color, Color) -> Unit = { label, fg, bg ->
//        Row(Modifier.background(bg)) {
//            Text(text=label, modifier= Modifier.padding(10.dp), color=fg)
//        }
//    }
//
//    Column {
//        Row { colors.forEach { Box(modifier = Modifier.size(50.dp).background(it)) } }
//        showColor("Surface Text", MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.surface)
//        showColor("Error Message", MaterialTheme.colorScheme.onError, MaterialTheme.colorScheme.error)
//        showColor("Primary text", MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.primary)
//        showColor("Background text", MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.background)
//        showColor("Secondary text", MaterialTheme.colorScheme.onSecondary, MaterialTheme.colorScheme.secondary)
//    }
//
//}
//
//@Preview(name = "small font", group = "font scales", fontScale = 0.5f)
//@Preview(name = "normal font", group = "font scales", fontScale = 1f)
//@Preview(name = "large font", group = "font scales", fontScale = 1.5f)
//annotation class FontScalePreviews
//
//@Preview(name = "light mode", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(name = "dark mode", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
//annotation class ThemePreviews
//
//@Preview(name = "portrait", group = "orientation", device = Devices.PIXEL, showSystemUi = true)
//@Preview(name = "landscape", group = "orientation", device = Devices.AUTOMOTIVE_1024p, showSystemUi = true)
//annotation class OrientationPreviews
//
//@Preview(name = "en", group = "locales", locale = "en")
//@Preview(name = "sk", group = "locales", locale = "sk")
//@Preview(name = "cs", group = "locales", locale = "cs")
//annotation class LocalePreviews
//
//@OrientationPreviews
//@ThemePreviews
//@FontScalePreviews
//@LocalePreviews
//annotation class CombinedPreviews
//
//@Composable
//@Preview
//private fun PalettePreview() {
//    Column {
//        FoodBoxLightTheme {
//            Palette()
//        }
//        FoodBoxDarkTheme {
//            Palette()
//        }
//    }
//}
//
//@Composable
//fun FoodBoxLightTheme(content: @Composable () -> Unit)
//        = MaterialTheme(colorScheme = lightColorScheme, content = content)
//
//@Composable
//fun FoodBoxDarkTheme(content: @Composable () -> Unit)
//        = MaterialTheme(colorScheme = darkColorScheme, content = content)
//
//@Composable
//fun FoodBoxTheme(content: @Composable () -> Unit) {
//    if (isSystemInDarkTheme()) {
//        FoodBoxDarkTheme {
//            content()
//        }
//    } else {
//        FoodBoxLightTheme {
//            content()
//        }
//    }
//}
//
//@Composable
//fun FoodBoxThemeWithSurface(modifier: Modifier, content: @Composable () -> Unit) {
//    FoodBoxTheme {
//        Surface(modifier) {
//            content()
//        }
//    }
//}
//
//@Composable
//fun FoodBoxThemeWithSurface(content: @Composable () -> Unit)
//        = FoodBoxThemeWithSurface(Modifier.fillMaxSize(), content)
