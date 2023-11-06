package com.krrish.pokeapicompose.ui.pokemonlist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.krrish.pokeapicompose.ui.pokemonlist.viewmodel.PokemonListState
import com.krrish.pokeapicompose.ui.pokemonlist.viewmodel.PokemonListViewModel

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {

    val pokemonListModel = viewModel.uiState.collectAsState(initial = PokemonListState())

    // Like RecyclerView but in Compose
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = pokemonListModel.value.isRefreshing),
        onRefresh = {
            viewModel.refresh()
        }) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp)
        ) {
            val itemCount = if (pokemonListModel.value.pokemonList.size % 2 == 0) {
                pokemonListModel.value.pokemonList.size / 2
            } else {
                pokemonListModel.value.pokemonList.size / 2 + 1
            }
            items(itemCount) {

                // Scroll down, load more Pokémons!
                if (it >= itemCount - 1
                    && !pokemonListModel.value.endReached
                    && !pokemonListModel.value.isLoading
                    && !pokemonListModel.value.isSearching
                ) {
                    LaunchedEffect(key1 = true) {
                        viewModel.loadPokemonList()
                    }
                }

                PokedexRow(
                    rowIndex = it,
                    entries = pokemonListModel.value.pokemonList,
                    navController = navController
                )
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val showDialog = remember { mutableStateOf(false) }

        if (pokemonListModel.value.isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        if (pokemonListModel.value.loadError.isNotEmpty()) {
            showDialog.value = true
        }

        if (showDialog.value) {
            DialogError(error = pokemonListModel.value.loadError, onRetry = {
                viewModel.loadPokemonList()
            }, {
                showDialog.value = it
            })
        }
    }
}
