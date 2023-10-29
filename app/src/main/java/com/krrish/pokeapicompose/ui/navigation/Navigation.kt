package com.krrish.pokeapicompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.krrish.pokeapicompose.ui.detail.components.PokemonDetailScreen
import com.krrish.pokeapicompose.ui.pokemonlist.components.PokemonListScreen
import com.krrish.pokeapicompose.util.Screens
import java.util.Locale

@Composable
fun Navigation() {

    val argPokemonName = "pokemonName"
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.PokemonListScreen) {

        composable(
            route = Screens.PokemonListScreen) {
            PokemonListScreen(navController = navController)
        }
        composable(
            route = "${Screens.PokemonDetailScreen}/{$argPokemonName}",
            arguments = listOf(
                navArgument(argPokemonName) {
                    type = NavType.StringType
                }
            )
        ) {
            val pokemonName = remember {
                it.arguments?.getString(argPokemonName)
            }
            PokemonDetailScreen(
                pokemonName = pokemonName?.lowercase(Locale.ROOT) ?: "",
                navController = navController
            )
        }
    }
}
