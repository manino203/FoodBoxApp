package com.example.foodboxapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.foodboxapp.navigation.ScreenDestination
import com.example.foodboxapp.viewmodels.MainViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate


@Composable
fun DestinationScreen(
    toolbarViewModel: ToolbarViewModel,
    mainViewModel: MainViewModel,
    navController: NavController<ScreenDestination>,
) {
    val destination by remember(navController.backstack.entries.last().destination) {
        mutableStateOf(navController.backstack.entries.last().destination)
    }
    val screen: @Composable () -> Unit = remember(destination) {
        val factory: (@Composable () -> Unit) -> @Composable () -> Unit = {
            @Composable { it() }
        }

        return@remember when (destination){
            is ScreenDestination.Login -> factory{
                LoginScreen(toolbarViewModel = toolbarViewModel, (destination as ScreenDestination.Login).username) {
                    navController.navigate(ScreenDestination.Registration)
                }
            }

            ScreenDestination.Main -> factory{
                Text("Main")
            }
            ScreenDestination.Registration -> factory{
                RegistrationScreen(toolbarViewModel = toolbarViewModel) {
                    navController.navigate(ScreenDestination.Login(""))
                }
            }
        }
    }
    screen()
}