package com.example.foodboxapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodboxapp.R
import com.example.foodboxapp.backend.repositories.SettingsRepositoryImpl
import com.example.foodboxapp.backend.repositories.SettingsRepositoryImpl.Companion.THEME_CHOICES
import com.example.foodboxapp.backend.repositories.SettingsState
import com.example.foodboxapp.viewmodels.SettingsViewModel
import com.example.foodboxapp.viewmodels.ToolbarViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    toolbarViewModel: ToolbarViewModel
) {
    val viewModel:SettingsViewModel = koinViewModel()
    val title = stringResource(id = R.string.settings)
    LaunchedEffect(title) {
        toolbarViewModel.updateTitle(title)
    }

    LaunchedEffect(Unit) {
        viewModel.collectChanges()
        toolbarViewModel.updateLoading(false)
    }

    SettingsScreen(
        viewModel.uiState.value,
        {
            viewModel.setTheme(it)
        },
        {
            viewModel.setLanguage(it)
        }
    )

}

@Composable
private fun SettingsScreen(
    uiState: SettingsState,
    actionSetTheme: (Int) -> Unit,
    actionSetLanguage: (Int) -> Unit
){
    LazyColumn {
        item{
            Category(title = stringResource(R.string.settings_appearance)){
                ChoiceSetting(
                    stringResource(id = R.string.settings_theme),
                    uiState.theme,
                    THEME_CHOICES,
                    actionSetTheme
                )
                ChoiceSetting(
                    stringResource(id = R.string.settings_language),
                    uiState.language,
                    SettingsRepositoryImpl.LANGUAGE_CHOICES,
                    actionSetLanguage
                )
            }
        }

    }
}

@Composable
private fun SettingItem(
    title: String,
    actionClick: () -> Unit,
    valueComposable: @Composable () -> Unit
){
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { actionClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(0.64f),
            text = title,
            fontWeight = FontWeight.Bold,
        )
        Box(
            Modifier.weight(0.33f),
            contentAlignment = Alignment.Center
        ){
            valueComposable()
        }
    }
}

@Composable
private fun Category(
    title: String,
    content: @Composable ColumnScope.() -> Unit
){
    Column{
        Text(
            modifier = Modifier.padding(16.dp),
            text = title,
            color = MaterialTheme.colorScheme.primary
        )
        this.content()
        HorizontalDivider()
    }
}

@Composable
private fun OnOffSetting(

){

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChoiceSetting(
    title: String,
    value: SettingsState.ChoiceSetting,
    choices: List<SettingsState.ChoiceSetting>,
    actionSet: (Int) -> Unit
){
    var sheetOpen by remember{
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val coroScope = rememberCoroutineScope()

    val dismiss: () -> Unit = {
        coroScope.launch{
            sheetState.hide()
        }.invokeOnCompletion {
            sheetOpen = false
        }
    }

    if(sheetOpen){
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = dismiss
        ) {
            Column(
                Modifier.padding(16.dp)
            ){
                choices.forEachIndexed { index, it ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (it == value) MaterialTheme.colorScheme.primary.copy(
                                    alpha = .3f
                                ) else MaterialTheme.colorScheme.surface, RoundedCornerShape(50)
                            )
                            .clickable {
                                actionSet(index)
                                dismiss()
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,

                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(it.label),
                            fontWeight = if (it == value) FontWeight.Bold else null,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                            )
                    }
                }
            }
        }
    }


    SettingItem(
        title = title,
        actionClick = {
            sheetOpen = true
            coroScope.launch {
                sheetState.show()
            }
        }
    ) {
        Text(text = stringResource(value.label))
    }
}


@Preview
@Composable
private fun SettingsScreenPreview(){
//    SettingsScreen(uiState = SettingsState())
}