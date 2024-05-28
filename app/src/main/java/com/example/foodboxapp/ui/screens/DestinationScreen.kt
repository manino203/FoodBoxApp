package com.example.foodboxapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.foodboxapp.navigation.ScreenDestination
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate


@Composable
fun DestinationScreen(
    toolbarViewModel: ToolbarViewModel,
    navController: NavController<ScreenDestination>,
    destination: ScreenDestination
) {

    val screen: @Composable () -> Unit = remember(destination) {
        val factory: (@Composable () -> Unit) -> @Composable () -> Unit = {
            @Composable { it() }
        }
        toolbarViewModel.updateVisibility(destination != ScreenDestination.Splash)

        return@remember when (destination){
            is ScreenDestination.Login -> factory{
                LoginScreen(toolbarViewModel = toolbarViewModel, destination.username) {
                    navController.navigate(ScreenDestination.Registration)
                }
            }

            ScreenDestination.Main -> factory{
                StoreScreen(toolbarViewModel){
                    navController.navigate(ScreenDestination.Store(it))
                }
            }
            ScreenDestination.Registration -> factory{
                RegistrationScreen(toolbarViewModel = toolbarViewModel) {
                    navController.navigate(ScreenDestination.Login(""))
                }
            }

            ScreenDestination.Splash -> factory{
                SplashScreen()
            }

            is ScreenDestination.Store -> factory{
                ProductScreen(destination.store, toolbarViewModel)
            }

            ScreenDestination.Cart -> factory{
                CartScreen(toolbarViewModel){
                    navController.navigate(ScreenDestination.Checkout)
                }
            }

            ScreenDestination.Settings -> factory{
                SettingsScreen(toolbarViewModel)
            }

            ScreenDestination.Checkout -> factory{
                CheckoutScreen(toolbarViewModel){
                    navController.navigate(ScreenDestination.OrderSent)
                }
            }

            ScreenDestination.OrderSent -> factory{
                OrderSentScreen(toolbarViewModel = toolbarViewModel)
            }
        }
    }
    screen()
}