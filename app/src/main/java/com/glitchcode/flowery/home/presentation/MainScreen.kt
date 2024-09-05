package com.glitchcode.flowery.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glitchcode.flowery.core.presentation.components.FloweryButton

@Composable
fun HomeScreen(
    onNavigateToLoginScreen: () -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
) {

    val isLoggedIn = viewModel.isLoggedIn.collectAsState()

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
        if (!isLoggedIn.value) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You are not logged in!",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}