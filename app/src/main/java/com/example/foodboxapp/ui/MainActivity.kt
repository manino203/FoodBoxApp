package com.example.foodboxapp.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodboxapp.backend.data_holders.AccountType
import com.example.foodboxapp.navigation.ScreenDestination
import com.example.foodboxapp.ui.composables.FoodBoxThemeWithSurface
import com.example.foodboxapp.ui.composables.NavigationDrawer
import com.example.foodboxapp.ui.composables.Toolbar
import com.example.foodboxapp.ui.screens.DestinationScreen
import com.example.foodboxapp.viewmodels.MainUiState
import com.example.foodboxapp.viewmodels.MainViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavAction
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController
import dev.olshevski.navigation.reimagined.replaceAll
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity: AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            val toolbarViewModel: ToolbarViewModel = koinViewModel()
            val viewModel: MainViewModel = koinViewModel()
            val scope = rememberCoroutineScope()
            val navController: NavController<ScreenDestination> = rememberNavController(
                startDestination = viewModel.uiState.value.screenDestination
            )
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

            LaunchedEffect(viewModel) {
                viewModel.collectAccount()
                viewModel.collectSessionState()
            }

            LaunchedEffect(toolbarViewModel) {
                if(viewModel.uiState.value.account?.type == AccountType.Client){
                    toolbarViewModel.loadCart()
                }
            }

            val onBackPressed: () -> Unit = remember(navController, drawerState, scope) {
                {
                    scope.launch {
                        if (drawerState.isOpen && !drawerState.isAnimationRunning) {
                            drawerState.close()
                        } else if (navController.backstack.entries.size > 1) {
                            navController.pop()
                        } else {
                            finish()
                        }
                    }
                }
            }
            val onBackPressedCallback by rememberUpdatedState(onBackPressed)

            LaunchedEffect(Unit) {
                onBackPressedDispatcher.addCallback {
                    onBackPressedCallback()
                }
            }

            FoodBoxThemeWithSurface{
                MainActivityContent(
                    viewModel.uiState.value,
                    toolbarViewModel = toolbarViewModel,
                    navController,
                    drawerState
                ) {
                    viewModel.logout()
                }
            }
        }
    }
}

@Composable
private fun MainActivityContent(
    uiState: MainUiState,
    toolbarViewModel: ToolbarViewModel,
    navController: NavController<ScreenDestination>,
    drawerState: DrawerState,
    actionLogout: () -> Unit
){



    LaunchedEffect(uiState.screenDestination) {
        navController.replaceAll(uiState.screenDestination)
    }


    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent ={
            NavigationDrawer(
                uiState,
                navController,
                actionLogout
            ) {
                scope.launch {
                    drawerState.apply {
                        close()
                    }
                }
            }
        },
        gesturesEnabled = drawerState.isOpen,
        content = {
            Scaffold(
                topBar = {
                    Toolbar(
                        uiState = toolbarViewModel.uiState.value,
                        navController
                    ) {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                },
                content = { padding ->
                    AnimatedNavHost(
                        modifier = Modifier.padding(padding),
                        controller = navController,
                        transitionSpec = { action, _, _ ->
                            val direction = if (action == NavAction.Pop) {
                                AnimatedContentTransitionScope.SlideDirection.End
                            } else {
                                AnimatedContentTransitionScope.SlideDirection.Start
                            }

                            ContentTransform(
                                slideIntoContainer(direction),
                                slideOutOfContainer(direction)
                            )
                        }
                    ) {
                        Box{
                            DestinationScreen(toolbarViewModel, navController, it)
                        }
                    }
                }
            )
        }
    )
}




@Composable
@Preview
private fun MainActivityContentPreview(){
    val viewModel: MainViewModel = viewModel()
    val toolbarViewModel: ToolbarViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController(startDestination = ScreenDestination.Splash as ScreenDestination)


    MainActivityContent(viewModel.uiState.value, toolbarViewModel, navController, drawerState){}
}
