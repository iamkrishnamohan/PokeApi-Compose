package com.krrish.pokeapicompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.krrish.pokeapicompose.ui.navigation.Navigation
import com.krrish.pokeapicompose.ui.theme.PokeApiComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(100)
            setContent {
                PokeApiComposeTheme {
                    Navigation()
                }
            }
        }
    }
}

