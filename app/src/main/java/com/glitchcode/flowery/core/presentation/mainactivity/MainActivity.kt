package com.glitchcode.flowery.core.presentation.mainactivity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import com.glitchcode.flowery.core.theme.FloweryTheme
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

            }
        }
    }

    override fun onDestroy() {
        viewModel.unregisterAppearancePrefsListener()
        super.onDestroy()
    }
}