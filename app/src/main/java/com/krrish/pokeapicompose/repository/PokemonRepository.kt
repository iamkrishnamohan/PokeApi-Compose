package com.krrish.pokeapicompose.repository

import com.krrish.pokeapicompose.data.remote.response.Pokemon
import com.krrish.pokeapicompose.data.remote.response.PokemonList
import com.krrish.pokeapicompose.util.Resource

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList>
    suspend fun getPokemonDetail(pokemonName: String): Resource<Pokemon>
}
