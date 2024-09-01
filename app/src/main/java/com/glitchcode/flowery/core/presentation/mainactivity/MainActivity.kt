package com.glitchcode.flowery.core.presentation.mainactivity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.glitchcode.flowery.core.theme.FloweryTheme
import com.glitchcode.flowery.login.presentation.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel.registerAppearancePrefsListener()

        setContent {
            val isUseSystemTheme = viewModel.isUseSystemColorTheme.collectAsState()
            val isUseDarkTheme = viewModel.isUseDarkTheme.collectAsState()
            val isUseDynamicColors = viewModel.isUseDynamicColors.collectAsState()

            FloweryTheme(
                useSystemTheme = isUseSystemTheme.value,
                useDarkTheme = isUseDarkTheme.value,
                useDynamicColor = isUseDynamicColors.value
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    val startDestination = viewModel.startDestination

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable("login-screen") {
                            LoginScreen(
                                onNavigateToMainScreen = {
                                    println("ACCOUNT CREATED")
//                                    navController.navigate("main-screen")
                                }
                            )
                        }
                        composable("main-screen") {

                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.unregisterAppearancePrefsListener()
        super.onDestroy()
    }
}