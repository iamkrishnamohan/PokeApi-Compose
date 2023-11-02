package com.krrish.pokeapicompose.ui.pokemonlist.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krrish.pokeapicompose.data.models.PokedexListEntry
import com.krrish.pokeapicompose.repository.PokemonRepositoryImpl
import com.krrish.pokeapicompose.ui.pokemonlist.usecases.pokedexListEntries
import com.krrish.pokeapicompose.ui.pokemonlist.usecases.searchPokedexListEntries
import com.krrish.pokeapicompose.util.Constants.PAGE_SIZE
import com.krrish.pokeapicompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepositoryImpl
) : ViewModel() {

    private var currentPage = 0

    private var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    val returnPokemonList = pokemonList

    private var loadError = mutableStateOf("")
    val returnLoadError = loadError

    private var isLoading = mutableStateOf(false)
    val returnIsLoading = isLoading

    private var endReached = mutableStateOf(false)
    val returnEndReached = endReached

    private var isRefreshing = mutableStateOf(false)
    val returnIsRefreshing = isRefreshing

    private var isSearching = mutableStateOf(false)
    val returnIsSearching = isSearching

    private var isSearchStarting = true

    private var cachedPokemonList = listOf<PokedexListEntry>()

    init {
        loadPokemonList()
    }

    fun refresh() {
        isRefreshing.value = true
        currentPage = 0 // Reset page
        loadPokemonList(true)
    }

    fun searchPokemonList(query: String) {
        val listToSearch = when {
            isSearchStarting -> {
                pokemonList.value
            }

            else -> {
                // If we typed at least one character
                cachedPokemonList
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            when {
                query.isEmpty() -> {
                    pokemonList.value = cachedPokemonList
                    isSearching.value = false
                    isSearchStarting = true
                    return@launch
                }

                // Update entries with the results
                else -> {
                    val results = searchPokedexListEntries(listToSearch, query)

                    if (isSearchStarting) {
                        cachedPokemonList = pokemonList.value
                        isSearchStarting = false
                    }

                    // Update entries with the results
                    pokemonList.value = results
                    isSearching.value = true
                }
            }
        }
    }


    fun loadPokemonList(fromRefresh: Boolean = false) {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)) {
                is Resource.Success -> {
                    when {
                        result.data != null -> {
                            endReached.value = currentPage * PAGE_SIZE >= result.data.count
                            val pokedexEntries = pokedexListEntries(result.data)
                            currentPage++

                            if (fromRefresh) {
                                pokemonList.value = pokedexEntries
                                isRefreshing.value = false
                            } else {
                                pokemonList.value += pokedexEntries
                            }
                        }
                    }
                    loadError.value = ""
                    isLoading.value = false
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }

                is Resource.Loading -> {

                }
            }
        }
    }
}

