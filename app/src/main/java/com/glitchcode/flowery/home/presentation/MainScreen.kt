package com.glitchcode.flowery.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.glitchcode.flowery.core.presentation.components.FloweryButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onNavigateToLoginScreen: () -> Unit,
    onLoginDataOutdated: () -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
) {

    LaunchedEffect(null) {
        viewModel.isLoggedIn.collectLatest { isLoggedIn ->
            if (!isLoggedIn) onLoginDataOutdated.invoke()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FloweryButton(
            onClick = {
                viewModel.logout(onNavigateToLoginScreen)
            }
        ) {
            Text(text = "Logout")
        }
    }
}