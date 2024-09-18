package com.glitchcode.flowery.core.presentation.mainactivity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.glitchcode.flowery.core.domain.utils.ScreenRoutes
import com.glitchcode.flowery.core.theme.FloweryTheme
import com.glitchcode.flowery.home.presentation.HomeScreen
import com.glitchcode.flowery.home.presentation.NotAuthorizedScreen
import com.glitchcode.flowery.login.presentation.LoginScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModel()

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
                        composable(ScreenRoutes.loginScreen) {
                            LoginScreen(
                                onNavigateToMainScreen = {
                                    navController.navigate(ScreenRoutes.mainScreen) {
                                        popUpTo(ScreenRoutes.loginScreen) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                        composable(ScreenRoutes.mainScreen) {
                            HomeScreen(
                                onNavigateToLoginScreen = {
                                    navController.navigate(ScreenRoutes.loginScreen) {
                                        popUpTo(ScreenRoutes.mainScreen) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onLoginDataOutdated = {
                                    navController.navigate(ScreenRoutes.loginOutdatetdScreen) {
                                        popUpTo(ScreenRoutes.mainScreen) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                        composable(ScreenRoutes.loginOutdatetdScreen) {
                            NotAuthorizedScreen(
                                onLoginClicked = {
                                    navController.navigate(ScreenRoutes.loginScreen) {
                                        popUpTo(ScreenRoutes.mainScreen) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
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