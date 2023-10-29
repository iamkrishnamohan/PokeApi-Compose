package com.krrish.pokeapicompose.ui.pokemonlist.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krrish.pokeapicompose.data.models.PokedexListEntry
import com.krrish.pokeapicompose.data.remote.response.Result
import com.krrish.pokeapicompose.repository.PokemonRepositoryImpl
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

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var isRefreshing = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokedexListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadPokemonList()
    }

    fun refresh() {
        isRefreshing.value = true
        currentPage = 0 // Reset page
        loadPokemonList(true)
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if (isSearchStarting) {
            pokemonList.value
        } else {
            // If we typed at least one character
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }

            val results = listToSearch.filter {
                // Search by name or pokédex number
                it.pokemonName.contains(query.trim(), true) ||
                        it.number.toString() == query.trim()
            }

            if (isSearchStarting) {
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }

            // Update entries with the results
            pokemonList.value = results
            isSearching.value = true
        }
    }

    fun loadPokemonList(fromRefresh: Boolean = false) {
        viewModelScope.launch {
            isLoading.value = true

            val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        endReached.value = currentPage * PAGE_SIZE >= result.data.count

                        val pokedexEntries = result.data.results.mapIndexed { _, entry ->
                            val number = getPokedexNumber(entry)
                            val url = getImageUrl(number)
                            PokedexListEntry(
                                entry.name.replaceFirstChar(Char::titlecase),
                                url,
                                number.toInt()
                            )
                        }

                        currentPage++

                        if (fromRefresh) {
                            pokemonList.value = pokedexEntries
                            isRefreshing.value = false
                        } else {
                            pokemonList.value += pokedexEntries
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

    fun getImageUrl(number: String): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
    }

    fun getPokedexNumber(entry: Result): String {
        return if (entry.url.endsWith("/")) {
            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
        } else {
            entry.url.takeLastWhile { it.isDigit() }
        }
    }
}
