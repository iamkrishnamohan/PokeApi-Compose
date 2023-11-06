package com.krrish.pokeapicompose.ui.pokemonlist.viewmodel

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PokemonListState(
    val pokemonList: List<PokedexListEntry> = emptyList(),
    val loadError: String = "",
    val isLoading: Boolean = false, val endReached: Boolean = false,
    val isRefreshing: Boolean = false, val isSearching: Boolean = false
)

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepositoryImpl
) : ViewModel() {

    private var currentPage = 0

    private val _uiState = MutableStateFlow(PokemonListState())
    val uiState: Flow<PokemonListState> = _uiState

    private var isSearchStarting = true

    private var cachedPokemonList = listOf<PokedexListEntry>()

    init {
        loadPokemonList()
    }

    fun refresh() {
        _uiState.update {
            it.copy(isRefreshing = true)
        }
        currentPage = 0 // Reset page
        loadPokemonList(true)
    }

    fun searchPokemonList(query: String) {
        when {
            isSearchStarting -> {
                _uiState.update {
                    it.copy(pokemonList = _uiState.value.pokemonList)
                }

            }

            else -> {
                // If we typed at least one character
                cachedPokemonList
            }
        }
        val listToSearch = _uiState.value.pokemonList
        viewModelScope.launch(Dispatchers.Default) {
            when {
                query.isEmpty() -> {
                    _uiState.update {
                        it.copy(pokemonList = cachedPokemonList, isSearching = false)
                    }
                    isSearchStarting = true
                    return@launch
                }

                // Update entries with the results
                else -> {
                    val results = searchPokedexListEntries(listToSearch, query)

                    if (isSearchStarting) {
                        _uiState.update {
                            it.copy(pokemonList = cachedPokemonList)
                        }
                        isSearchStarting = false
                    }

                    // Update entries with the results
                    _uiState.update {
                        it.copy(pokemonList = results, isSearching = true)
                    }
                }
            }
        }
    }


    fun loadPokemonList(fromRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            when (val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)) {
                is Resource.Success -> {
                    when {
                        result.data != null -> {
                            _uiState.update {
                                it.copy(endReached = currentPage * PAGE_SIZE >= result.data.count)
                            }
                            val pokedexEntries = pokedexListEntries(result.data)
                            currentPage++

                            if (fromRefresh) {
                                _uiState.update {
                                    it.copy(pokemonList = pokedexEntries, isRefreshing = false)
                                }
                            } else {
                                _uiState.update {
                                    it.copy(pokemonList = pokedexEntries)
                                }
                            }
                        }
                    }
                    _uiState.update {
                        it.copy(
                            loadError = "", isLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            loadError = result.message!!, isLoading = false
                        )
                    }
                }

                is Resource.Loading -> Unit
            }
        }
    }
}


