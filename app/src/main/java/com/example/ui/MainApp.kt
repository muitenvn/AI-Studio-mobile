package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.StudioWebScreen
import com.example.ui.theme.LiquidBgDark
import com.example.viewmodel.StudioViewModel

@Composable
fun MainApp(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LiquidBgDark,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        StudioWebScreen(
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize()
        )
    }
}
