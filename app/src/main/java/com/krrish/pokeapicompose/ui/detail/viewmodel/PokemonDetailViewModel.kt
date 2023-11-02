package com.krrish.pokeapicompose.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.krrish.pokeapicompose.data.remote.response.Pokemon
import com.krrish.pokeapicompose.repository.PokemonRepositoryImpl
import com.krrish.pokeapicompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepositoryImpl
) : ViewModel() {

    suspend fun getPokemonDetail(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonDetail(pokemonName)
    }
}


