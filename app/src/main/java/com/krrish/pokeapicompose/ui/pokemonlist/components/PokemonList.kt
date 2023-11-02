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
import com.krrish.pokeapicompose.ui.pokemonlist.viewmodel.PokemonListViewModel

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.returnPokemonList }
    val endReached by remember { viewModel.returnEndReached }
    val loadError by remember { viewModel.returnLoadError }
    val isLoading by remember { viewModel.returnIsLoading }
    val isSearching by remember { viewModel.returnIsSearching }
    val isRefreshing by remember { viewModel.returnIsRefreshing }

    // Like RecyclerView but in Compose

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = isRefreshing), onRefresh = {
        viewModel.refresh()
    }) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp)
        ) {
            val itemCount = if (pokemonList.size % 2 == 0) {
                pokemonList.size / 2
            } else {
                pokemonList.size / 2 + 1
            }
            items(itemCount) {

                // Scroll down, load more Pokémons!
                if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                    LaunchedEffect(key1 = true) {
                        viewModel.loadPokemonList()
                    }
                }

                PokedexRow(
                    rowIndex = it,
                    entries = pokemonList,
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

        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        if (loadError.isNotEmpty()) {
            showDialog.value = true
        }

        if (showDialog.value) {
            DialogError(error = loadError, onRetry = {
                viewModel.loadPokemonList()
            }, {
                showDialog.value = it
            })
        }
    }
}
