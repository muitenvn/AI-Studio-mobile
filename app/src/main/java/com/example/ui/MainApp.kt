package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.HubScreen
import com.example.ui.screens.ModelGuideScreen
import com.example.ui.screens.PromptLibraryScreen
import com.example.ui.screens.StudioWebScreen
import com.example.ui.theme.GeminiBlue
import com.example.ui.theme.StudioPrimary
import com.example.viewmodel.ScreenTab
import com.example.viewmodel.StudioViewModel

@Composable
fun MainApp(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    // Hardware back press: if on sub-tab, return to AI Studio Web view
    BackHandler(enabled = currentScreen != ScreenTab.STUDIO_WEB) {
        viewModel.setScreen(ScreenTab.STUDIO_WEB)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                tonalElevation = 3.dp,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = currentScreen == ScreenTab.STUDIO_WEB,
                    onClick = { viewModel.setScreen(ScreenTab.STUDIO_WEB) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == ScreenTab.STUDIO_WEB) Icons.Filled.Language else Icons.Outlined.Language,
                            contentDescription = "AI Studio Web"
                        )
                    },
                    label = { Text("AI Studio") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = StudioPrimary,
                        selectedTextColor = StudioPrimary,
                        indicatorColor = StudioPrimary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_studio")
                )

                NavigationBarItem(
                    selected = currentScreen == ScreenTab.HUB,
                    onClick = { viewModel.setScreen(ScreenTab.HUB) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == ScreenTab.HUB) Icons.Filled.Hub else Icons.Outlined.Hub,
                            contentDescription = "Studio Hub"
                        )
                    },
                    label = { Text("Hub") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = StudioPrimary,
                        selectedTextColor = StudioPrimary,
                        indicatorColor = StudioPrimary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_hub")
                )

                NavigationBarItem(
                    selected = currentScreen == ScreenTab.PROMPTS,
                    onClick = { viewModel.setScreen(ScreenTab.PROMPTS) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == ScreenTab.PROMPTS) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Prompt Scratchpad"
                        )
                    },
                    label = { Text("Prompts") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = StudioPrimary,
                        selectedTextColor = StudioPrimary,
                        indicatorColor = StudioPrimary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_prompts")
                )

                NavigationBarItem(
                    selected = currentScreen == ScreenTab.MODELS,
                    onClick = { viewModel.setScreen(ScreenTab.MODELS) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == ScreenTab.MODELS) Icons.Filled.AutoAwesome else Icons.Outlined.Tune,
                            contentDescription = "Models & Guide"
                        )
                    },
                    label = { Text("Models") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = StudioPrimary,
                        selectedTextColor = StudioPrimary,
                        indicatorColor = StudioPrimary.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_item_models")
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier.padding(innerPadding)
        ) { screen ->
            when (screen) {
                ScreenTab.STUDIO_WEB -> StudioWebScreen(viewModel = viewModel)
                ScreenTab.HUB -> HubScreen(viewModel = viewModel)
                ScreenTab.PROMPTS -> PromptLibraryScreen(viewModel = viewModel)
                ScreenTab.MODELS -> ModelGuideScreen(viewModel = viewModel)
            }
        }
    }
}
