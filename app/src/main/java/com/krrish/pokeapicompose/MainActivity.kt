package com.krrish.pokeapicompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.krrish.pokeapicompose.ui.navigation.Navigation
import com.krrish.pokeapicompose.ui.theme.PokeApiComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeApiComposeTheme {
                Navigation()
            }
        }
    }
}


