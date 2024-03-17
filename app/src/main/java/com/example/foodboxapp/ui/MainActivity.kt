package com.example.foodboxapp.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodboxapp.R
import com.example.foodboxapp.ui.composables.FoodBoxThemeWithSurface
import com.example.foodboxapp.ui.composables.NavigationDrawer
import com.example.foodboxapp.ui.screens.DestinationScreen
import com.example.foodboxapp.viewmodels.MainViewModel
import com.example.foodboxapp.viewmodels.ToolbarUiState
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import kotlinx.coroutines.launch


class MainActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        val viewModel: MainViewModel by viewModels()
        val toolbarViewModel: ToolbarViewModel by viewModels()
        setContent{
            FoodBoxThemeWithSurface{
                MainActivityContent(viewModel = viewModel, toolbarViewModel = toolbarViewModel)
            }
        }
    }
}

@Composable
private fun MainActivityContent(viewModel: MainViewModel, toolbarViewModel: ToolbarViewModel){
    val drawerState = rememberDrawerState(initialValue =DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent ={
            NavigationDrawer {
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
                    Toolbar(uiState = toolbarViewModel.uiState.value) {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                },
                content = {
                    DestinationScreen(toolbarViewModel, Modifier.padding(it))
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(uiState: ToolbarUiState, onMenuOpen: () -> Unit){
    Column{
        TopAppBar(
            title = { Text(uiState.title) },
            navigationIcon = {
                IconButton(onClick = onMenuOpen) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Menu"
                    )
                }
            }
        )
        LinearProgressIndicator(
            Modifier
                .fillMaxWidth()
                .alpha(if (uiState.loading) 1f else 0f)
        )
    }
}

@Composable
@Preview
private fun MainActivityContentPreview(){
    val viewModel: MainViewModel = viewModel()
    val toolbarViewModel: ToolbarViewModel = viewModel()
    MainActivityContent(viewModel, toolbarViewModel)
}
